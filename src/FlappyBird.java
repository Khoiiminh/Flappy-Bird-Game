import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; //this stores all the pipes and game
import java.util.Random;  // place the pipes at random positions
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    //Images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //Bird
    int birdX = boardWidth/8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;

        //Image field
        Image img;
        Bird(Image img){
            this.img = img;
        }
    }

    //Pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    
    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img){
            this.img = img;

        }
    } 

    //game logic
    Bird bird;
    int velocityX = -4;  //move pipes to the left speed 
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipeTimer;
    boolean gameOver = false;
    double score=0;


    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        //setBackground(Color.blue);

        //
        setFocusable(true);
        addKeyListener(this);
        //load images

        //.getImage() 'cause the image is .png file not string
        backgroundImg = new ImageIcon(getClass().getResource("Image_URL")).getImage();
        /*  -getClass().getResource("./flappybirdbg.png"): This part retrieves the URL of the resource named 
        "flappybirdbg.png" that is located in the same directory as the class file (or relative to the classpath if 
        the resources are packaged with the application).
            -new ImageIcon(...).getImage(): This part creates a new ImageIcon object using the URL obtained in the 
        previous step, and then calls getImage() to extract the Image object from the ImageIcon. */

        birdImg = new ImageIcon(getClass().getResource("Image_URL")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("Image_URL")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("Image_URL")).getImage();

        //Bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        //place pipe timer
        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });  //1000 is 1 second; 1500 is 1.5 seconds. Every 1.5s i'm going to call an action
        placePipeTimer.start();

        //game timer
        gameLoop = new Timer(1000/60, this);  // this related to actionPerformed() method
        gameLoop.start();

    }

    public void placePipes(){
        //Math.random()*(pipeHeight/2) = (0-1)*(256)
        //pipeHeight/4 = 128
        //total = 0-128-(0-256) --> 1/4pipeHeight -> 3/4pipeHeight   
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2)); 
        int openingSpace = boardHeight/4;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //background    
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        //bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        //Pipes
        for(int i=0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
        //score 
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver){
            g.drawString("Game Over"+ String.valueOf((int)score), 10, 35);
        }else{
            g.drawString(String.valueOf((int)score), 10, 35);
        }
    }

    public void move(){
        //bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y,0);

        //Pipes
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipeX += velocityX;

            if(!pipe.passed && bird.x > pipe.x+pipeWidth){
                pipe.passed = true;
                score += 0.5;
            }


            if (collision(bird, pipe)){
                gameOver = true;
            }
        }

        if (bird.y > boardHeight){
            gameOver = true;
        }
        
    }
    public boolean collision(Bird a, Pipe b){
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
           a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
           a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
           a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move(); // update the position of the bird
        repaint(); // this call the paintcomponent 
        if (gameOver){
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;
            if(gameOver){
                //reset the game by the conditions
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipeTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

   
}
