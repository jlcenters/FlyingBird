package flyingbird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.Timer;

public class FlyingBird implements ActionListener, MouseListener, KeyListener {

	public static FlyingBird flyingBird;
	private final int WIDTH = 800, HEIGHT = 800;
	private Renderer renderer;
	private Rectangle bird;
	private ArrayList<Rectangle> pipes;
	private int ticks, yMotion, score;
	private boolean gameOver, started;
	private Random rand;

	public FlyingBird() {
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);

		renderer = new Renderer();
		rand = new Random();

		jframe.add(renderer);
		jframe.setTitle("Flying Bird");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);

		bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
		pipes = new ArrayList<>();

		addPipe(true);
		addPipe(true);
		addPipe(true);
		addPipe(true);

		timer.start();
	}

	public void addPipe(boolean start) {
		int space = 300;
		int width = 100;
		int height = 50 + rand.nextInt(300);

		if (start) {
			pipes.add(new Rectangle(WIDTH + width + pipes.size() * 300, HEIGHT - height - 120, width, height));
			pipes.add(new Rectangle(WIDTH + width + (pipes.size() - 1) * 300, 0, width, HEIGHT - height - space));

		} else {
			pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x + 600, HEIGHT - height - 120, width, height));
			pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x, 0, width, HEIGHT - height - space));
		}
	}

	public void paintPipe(Graphics g, Rectangle pipe) {
		g.setColor(Color.green.darker());
		g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
	}

	public void jump() {
		if (gameOver) {
			bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
			pipes.clear();
			yMotion = 0;
			score = 0;

			addPipe(true);
			addPipe(true);
			addPipe(true);
			addPipe(true);

			gameOver = false;
		}

		if (!started) {
			started = true;
		} else if (!gameOver) {
			if (yMotion > 0) {
				yMotion = 0;
			}

			yMotion -= 10;
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		int speed = 10;

		ticks++;

		if (started) {
			for (int i = 0; i < pipes.size(); i++) {
				Rectangle pipe = pipes.get(i);

				pipe.x -= speed;
			}

			if (ticks % 2 == 0 && yMotion < 15) {
				yMotion += 2;
			}

			for (int i = 0; i < pipes.size(); i++) {
				Rectangle pipe = pipes.get(i);
				if (pipe.x + pipe.width == 0) {
					pipes.remove(pipe);
					if (pipe.y == 0) {
						addPipe(false);
					}
				}
			}

			bird.y += yMotion;

			for (Rectangle pipe : pipes) {
				if (pipe.y == 0 && ((bird.x + bird.width / 2) > (pipe.x + pipe.width / 2 - 10))
						&& ((bird.x + bird.width / 2) < (pipe.x + pipe.width / 2 + 10))) {
					score++;
				}

				if (pipe.intersects(bird)) {
					gameOver = true;

					if (bird.x <= pipe.x) {
						bird.x = pipe.x - bird.width;
					} else {
						if (pipe.y != 0) {
							bird.y = pipe.y - bird.height;
						} else if (bird.y < pipe.height) {
							bird.y = pipe.height;
						}
					}
				}
			}

			if (bird.y > HEIGHT - 120 || bird.y < 0) {
				gameOver = true;
			}

			if (bird.y + yMotion >= HEIGHT - 120) {
				bird.y = HEIGHT - 120 - bird.height;
				gameOver = true;
			}

		}

		renderer.repaint();
	}

	public void repaint(Graphics g) {
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT - 120, WIDTH, 120);

		g.setColor(Color.green);
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);

		g.setColor(Color.red);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);

		for (Rectangle pipe : pipes) {
			paintPipe(g, pipe);
		}

		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 100));

		if (!started) {
			g.drawString("Click to Begin", 75, HEIGHT / 2 - 50);
		}

		if (gameOver) {
			g.drawString("Game Over", 100, HEIGHT / 2 - 50);
		}

		if (!gameOver && started) {
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
		}

	}

	public static void main(String[] args) {
		flyingBird = new FlyingBird();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		jump();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_SPACE) {
			jump();
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

}
