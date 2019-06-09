package alchemystics.game.screen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import alchemystics.abstractclasses.AbstractLayoutManager;
import alchemystics.game.data.GamePlayData;
import alchemystics.game.data.LoadedGameData;
import alchemystics.game.data.MergeNode;
import alchemystics.main.Main;
import alchemystics.main.Utils;

public class GamePanel extends JLayeredPane {
	private static final long serialVersionUID = 8589853683621140327L;
	private GamePlayData data;
	public static LoadedGameData gameData;

	public GamePanel(GamePlayData data) {
		this.data = data;
		data.panel = this;
	}

	public CraftingComponent craftingComponent;
	public ItemsPanel itemsPanel;
	public JScrollPane scrollPanel;
	public ItemsContent contentPanel;
	public JLabel count;
	public JLabel textAlarm;
	public int currentLayer = 0;

	public void initializeComponents() {
		setBackground(Color.black);

		JLabel alarm = new JLabel();
		alarm.setText("");
		alarm.setFont(Main.defaultFont.deriveFont(1, 30));
		alarm.setForeground(new Color(100, 100, 100));
		add(alarm);
		textAlarm = alarm;

		JLabel collected = new JLabel();
		collected.setFont(Main.defaultFont.deriveFont(0, 130));
		collected.setText("0/0");
		collected.setForeground(new Color(0, 120, 250));
		add(collected);
		count = collected;

		CraftingComponent craft = new CraftingComponent(GamePanel.gameData);
		craft.setBackground(new Color(246, 246, 246));
		setLayer(craft, 0);
		add(craft);
		craftingComponent = craft;

		boolean newScroll = true;
		JScrollPane jsp = new JScrollPane();
		jsp.setBorder(null);
		JScrollBar verticalScrollBar = jsp.getVerticalScrollBar();
		if (newScroll) {
			verticalScrollBar.setUnitIncrement(0);
			jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		} else {
			verticalScrollBar.setUnitIncrement(30);
			jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		}
		if (newScroll)
			jsp.addMouseWheelListener(new MouseWheelListener() {

				int amount = 0;
				boolean thread = false;

				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					int move = 0;
					if (amount == 0) {
						move = 10;
					} else {
						move = Math.abs(amount) / 20 + 10;
					}
					if (e.getWheelRotation() < 0) {
						move *= -1;
					}
					boolean a0 = move < 0;
					boolean b0 = amount < 0;
					if (a0 != b0 && amount != 0) {
						amount = 0;
						move = 0;
					}
					amount += move;
					if (!thread) {
						thread = true;
						new Thread(new Runnable() {

							@Override
							public void run() {
								while (amount != 0) {
									int before = verticalScrollBar.getValue();
									if (amount < -60) {
										amount = -60;
									} else if (amount > 60)
										amount = 60;
									verticalScrollBar.setValue(verticalScrollBar.getValue() + amount);
									int after = verticalScrollBar.getValue();
									if (after == before) {
										amount = 0;
										break;
									}
									if (amount < 0)
										amount++;
									else
										amount--;
									Utils.sleep(10);
								}
								thread = false;
							}
						}).start();
					}
				}
			});
		setLayer(jsp, 0);
		add(jsp);
		scrollPanel = jsp;

		ItemsPanel items = new ItemsPanel(data);
		items.setBackground(new Color(240, 240, 240));
		jsp.setViewportView(items);
		itemsPanel = items;

		ItemsContent ic = new ItemsContent();
		setLayer(ic, 100);
		add(ic);
		contentPanel = ic;

		homebar = new HomeBar(new HomeBarCommander() {

			@Override
			public void showhints(boolean add) {
				boolean found = false;
				for (MergeNode mg : gameData.mergeNodes) {
					if (data.save.unlocked.contains(mg.result))
						continue;
					if (!data.save.unlocked.contains(mg.a))
						continue;
					if (!data.save.unlocked.contains(mg.b))
						continue;
					textAlarm.setText(getItemName(mg.a) + " + " + getItemName(mg.b) + " => " + getItemName(mg.result));
					found = true;
					if (add) {
						ImageVisibleItem ivi = getNewImageVisibleItem(mg.a);
						contentPanel.add(ivi);
						ivi.setLocation(getWidth() / 2 - 150, 500);
						contentPanel.setLayer(ivi, ++currentLayer);

						ivi = getNewImageVisibleItem(mg.b);
						contentPanel.add(ivi);
						ivi.setLocation(getWidth() / 2 + 150 - ivi.getWidth(), 500);
						contentPanel.setLayer(ivi, ++currentLayer);
					}
					break;
				}
				if (!found) {
					textAlarm.setText("아무것도 찾지 못했어요. :(");
				}
				hideAlarmLater();
			}

			public ImageVisibleItem getNewImageVisibleItem(int itemcode) {
				ImageVisibleItem ivi = new ImageVisibleItem(itemcode);
				ivi.setSize(items.getComponent(0).getSize());
				ivi.setLocation(0, 0);
				ItemMouseHandler imh = ItemMouseHandler.getNewOne(ivi, getItemMouseListener());
				ivi.addMouseListener(imh);
				ivi.addMouseMotionListener(imh);
				contentPanel.setLayer(ivi, ++currentLayer);
				return ivi;
			}

			@Override
			public void clearscreen() {
				new Thread(() -> {
					LinkedList<Component> remove = new LinkedList<>();
					boolean hy = false;
					while (contentPanel.getComponentCount() != 0 && (homebar.yForce || !hy)) {
						if (homebar.yForce)
							hy = true;
						for (Component comp : contentPanel.getComponents()) {
							if (comp.getY() > homebar.drawY)
								remove.add(comp);
						}
						while (!remove.isEmpty()) {
							Component removeFirst = remove.removeFirst();
							if (homebar.drawY < 0) {
								contentPanel.remove(removeFirst);
							} else {
								removeFirst.setLocation(removeFirst.getX(),
										homebar.drawY - removeFirst.getHeight() * 2);
							}
						}
					}
					contentPanel.removeAll();
				}).start();
				hideAlarmLater();
			}

			@Override
			public void randomItems(int count) {
				for (int i = 0; i < count; i++) {
					ArrayList<Integer> d = data.save.unlocked;
					ImageVisibleItem ivi = getNewImageVisibleItem(d.get((int) (Math.random() * d.size())));
					contentPanel.add(ivi);
					ivi.setLocation((int) (Math.random() * (craftingComponent.getWidth() - ivi.getWidth())),
							(int) (Math.random() * (contentPanel.getHeight() - ivi.getHeight())));
					contentPanel.setLayer(ivi, ++currentLayer);
				}
				textAlarm.setText("DEV: " + count + " 아이템 생성");
			}
		});
		setLayer(homebar, 150);
		add(homebar);

		setLayout(new AbstractLayoutManager() {
			@Override
			public void layoutContainer(Container parent) {
				craftingComponent.setLocation(0, 0);
				scrollPanel.setLocation(getWidth() / 10 * 7, 0);
				craftingComponent.setSize(getWidth() / 10 * 7, getHeight());
				scrollPanel.setSize(getWidth() / 10 * 3, getHeight());
				count.setText(data.save.unlocked.size() + "/" + gameData.items.size());
				count.setSize(count.getPreferredSize());
				count.setLocation(0, getHeight() - count.getHeight());
				alarm.setSize(alarm.getPreferredSize());
				alarm.setLocation((getWidth() - alarm.getWidth()) / 2, getHeight() / 2 + getHeight() / 4);
				ic.setLocation(0, 0);
				ic.setSize(getSize());
				homebar.setBounds(craftingComponent.getBounds());
			}
		});

		ItemMouseChecker imc = getItemMouseListener();

		validate();
		items.layout(jsp.getViewport().getSize(), imc);
	}

	public String getItemName(int code) {
		return gameData.languageKeys.get(gameData.items.get(code).key);
	}

	boolean alarmThreadRunning = false;
	long alarmCloseTime = 0;
	private HomeBar homebar;

	public void hideAlarmLater() {
		alarmCloseTime = System.currentTimeMillis() + 5000;
		if (!alarmThreadRunning) {
			alarmThreadRunning = true;
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (alarmCloseTime > System.currentTimeMillis()) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}
					}
					textAlarm.setText("");
					alarmThreadRunning = false;
				}
			}).start();
		}
	}

	public ItemMouseChecker getItemMouseListener() {
		return new ItemMouseChecker() {

			Point dragStartPos = null;
			Point dragStartComponent = null;
			ImageVisibleItem ivi;

			@Override
			public void mouseReleased(MouseEvent e, PaintableItem item) {
				if (item instanceof CraftableItem)
					item.highlighted = false;

				new Thread(() -> {
					for (Component jc : contentPanel.getComponents()) {
						if (false == jc instanceof PaintableItem)
							continue;
						((PaintableItem) jc).highlighted = false;
						jc.repaint();
					}
				}).start();

				if (ivi.getX() + ivi.getWidth() > contentPanel.getWidth() / 100 * 70) {
					contentPanel.remove(ivi);
					return;
				}

				ItemMouseHandler imh = ItemMouseHandler.getNewOne(ivi, getItemMouseListener());
				for (MouseListener ml : ivi.getMouseListeners()) {
					ivi.removeMouseListener(ml);
				}
				for (MouseMotionListener ml : ivi.getMouseMotionListeners()) {
					ivi.removeMouseMotionListener(ml);
				}
				ivi.addMouseListener(imh);
				ivi.addMouseMotionListener(imh);

				for (Component jc : contentPanel.getComponents()) {
					if (false == jc instanceof ImageVisibleItem)
						continue;
					if (isSameComp(jc))
						continue;
					checkCollideAndProcess(ivi, (ImageVisibleItem) jc, true);
				}
			}

			@Override
			public void mousePressed(MouseEvent e, PaintableItem item) {
				if (item instanceof CraftableItem) {
					item.highlighted = true;
					dragStartPos = e.getPoint();
					ivi = new ImageVisibleItem(item.itemcode);
					contentPanel.add(ivi);
					ivi.setSize(item.getSize());
					ivi.setLocation(e.getLocationOnScreen().x - e.getX(), e.getLocationOnScreen().y - e.getY());
					contentPanel.setLayer(ivi, ++currentLayer);
					dragStartComponent = ivi.getLocation();
				} else if (item instanceof ImageVisibleItem) {
					dragStartPos = e.getPoint();
					ivi = (ImageVisibleItem) item;
					ivi.setLocation(e.getLocationOnScreen().x - e.getX(), e.getLocationOnScreen().y - e.getY());
					contentPanel.setLayer(ivi, ++currentLayer);
					dragStartComponent = ivi.getLocation();
				}
			}

			@Override
			public void mouseDragged(MouseEvent e, PaintableItem item) {
				ivi.setLocation(dragStartComponent.x + e.getXOnScreen() - dragStartComponent.x - dragStartPos.x,
						dragStartComponent.y + e.getYOnScreen() - dragStartComponent.y - dragStartPos.y);
				for (Component jc : contentPanel.getComponents()) {
					if (false == jc instanceof ImageVisibleItem)
						continue;
					if (isSameComp(jc))
						continue;
					checkCollideAndProcess(ivi, (ImageVisibleItem) jc, false);
				}
			}

			private boolean isSameComp(Component jc) {
				return jc == ivi && jc.getLocation().equals(ivi.getLocation());
			}

			public boolean checkCollideAndProcess(ImageVisibleItem ivi, ImageVisibleItem target, boolean released) {
				if (target.getLeft() < ivi.getRight() && target.getRight() > ivi.getLeft()
						&& target.getTop() < ivi.getBottom() && target.getBottom() > ivi.getTop()) {
					if (released) {
						int suc = 0;
						Set<Integer> st = new HashSet<>();
						int news = 0;
						for (MergeNode mn : gameData.mergeNodes) {
							if ((ivi.itemcode == mn.a && target.itemcode == mn.b)
									|| (ivi.itemcode == mn.b && target.itemcode == mn.a)) {
								System.out.println(ivi.itemcode + " + " + target.itemcode + " => " + mn.result);
								if (st.contains(mn.result)) {
									continue;
								}
								st.add(mn.result);
								suc++;
								news++;
								ImageVisibleItem ivi2 = new ImageVisibleItem(mn.result);
								ivi2.setSize(ivi.getSize());
								ivi2.setLocation(ivi.getX() + suc * ivi.getWidth() / 10,
										ivi.getY() + suc * ivi.getHeight() / 10);
								contentPanel.add(ivi2);
								if (data.save.unlocked.contains(ivi2.itemcode)) {
									news--;
								} else {
									data.save.unlocked.add(ivi2.itemcode);
								}

								ItemMouseHandler imh = ItemMouseHandler.getNewOne(ivi2, getItemMouseListener());
								ivi2.addMouseListener(imh);
								ivi2.addMouseMotionListener(imh);
							}
						}
						if (suc != 0) {
							contentPanel.remove(ivi);
							contentPanel.remove(target);
							if (news != 0) {
								Collections.sort(data.save.unlocked);
								data.data.addNewItem();
								return true;
							}
							return true;
						}
					}
					target.highlighted = true;
				} else {
					target.highlighted = false;
				}
//				ivi.highlighted = target.highlighted;
				return false;
			}
		};
	}

	private interface HomeBarCommander {
		public void clearscreen();

		public void showhints(boolean add);

		public void randomItems(int count);
	}

	private class HomeBar extends JPanel {
		private static final long serialVersionUID = 0;

		private boolean pressed = false;
		private boolean moved = false;
		private long pressTime = 0;
		private int drawY = 0;
		private boolean yForce = false;
		private long zoomTime = 0;

		public HomeBar(HomeBarCommander command) {
			setOpaque(false);
			MouseAdapter mouseAdapter = new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getY() > getHeight() - 40) {
						pressed = true;
						moved = false;
						yForce = false;
						pressTime = System.currentTimeMillis();
					}
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					pressed = false;
					int pressDuration = (int) (System.currentTimeMillis() - pressTime);
					if (pressDuration > 1000 && moved) {
						command.randomItems((pressDuration) / 50);
						return;
					}
					if (!moved) {
						command.showhints(pressDuration > 300);
					}
					if (e.getY() < getHeight() / 5 * 4) {
						pressed = false;
						command.clearscreen();
						new Thread(() -> {
							int mod = 20;
							yForce = true;
							while (drawY > -50) {
								drawY -= mod++;
								Utils.sleep(10);
							}
							yForce = false;
						}).start();
					}
				}

				@Override
				public void mouseDragged(MouseEvent e) {
					if (!pressed)
						return;
					if (e.getY() < getHeight() - 45) {
						moved = true;
					}
					drawY = e.getY() - 7;
				}
			};
			addMouseListener(mouseAdapter);
			addMouseMotionListener(mouseAdapter);
		}

		@Override
		public boolean contains(int x, int y) {
			return y > getHeight() - 40 && x < craftingComponent.getWidth();
		}

		@Override
		public boolean contains(Point p) {
			return contains(p.x, p.y);
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			if (pressed) {
				long pressDuration = System.currentTimeMillis() - pressTime;
				pressDuration /= 3;
				int v = (int) Math.min(100, pressDuration);
				g2.setColor(new Color(v, v, v));
			} else
				g2.setColor(Color.black);
			int drawWidth = 400;
			int drawHeight = 25;
			if (!moved) {
				zoomTime = System.currentTimeMillis();
			}
			if (pressed && moved) {
				long pressDuration = System.currentTimeMillis() - zoomTime;
				pressDuration /= 6;
				drawWidth += Math.min(pressDuration, 50);
			}
			int dy = getHeight() - 30;
			if ((pressed && moved) || yForce) {
				dy = drawY;
			}
			g2.fillRect((getWidth() - drawWidth) / 2, dy, drawWidth, drawHeight);
		}
	}
}
