/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TheLastSpaceShipThongNguyen;

//Import all JavaFX utilities to maake the game
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import TheLastSpaceShipThongNguyen.TheLastSpaceshipThongNguyen.Sprite;
import javafx.scene.canvas.Canvas;

/**
 *
 * @author Thong Nguyen Project: The Last Spaceship Date: 04/28/2017
 */
public class TheLastSpaceshipThongNguyen extends Application {

    @Override
    public void start(Stage primaryStage) {

        //INITIALIZATION*********
        //**SET UP WINDOW
        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        Canvas canvas = new Canvas(600, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        primaryStage.setTitle("The Last Spaceship");

        //**INITIALIZE GAME FACTORS
        IntValue gameStatus = new IntValue(0);
        //0. Menu
        //1. In-game
        //2. End
        //3. Help

        IntValue result = new IntValue(0);
        //0. None
        //1. Win
        //2. Lose  

        IntValue phase = new IntValue(0);
        //0. Mob enemies
        //1. Boss

        IntValue round = new IntValue(0); //Number of round played

        //**IMPORT DATA AND CREATE + INITIALIZE OBJECTS
        Sprite background = new Sprite();
        Image[] backgroundFrames = new Image[10];
        for (int i = 0; i < 10; i++) {
            backgroundFrames[i] = new Image("background" + (i + 1) + ".png");
        }
        background.setFrames(backgroundFrames, 0.2);
        background.setPosition(0, 0);

        //Buttons and Captions
        Sprite title = new Sprite();
        Image[] titleFrames = new Image[6];
        for (int i = 0; i < 6; i++) {
            titleFrames[i] = new Image("title" + (i + 1) + ".png");
        }
        title.setFrames(titleFrames, 0.1);
        title.setPosition(60, 100);
        Sprite lostTitle = new Sprite();
        Image[] lostTitleFrames = new Image[3];
        for (int i = 0; i < 3; i++) {
            lostTitleFrames[i] = new Image("lost_title" + (i + 1) + ".png");
        }
        lostTitle.setFrames(lostTitleFrames, 0.1);
        lostTitle.setPosition(115, 180);

        Sprite wonTitle = new Sprite();
        Image[] wonTitleFrames = new Image[3];
        for (int i = 0; i < 3; i++) {
            wonTitleFrames[i] = new Image("won_title" + (i + 1) + ".png");
        }
        wonTitle.setFrames(wonTitleFrames, 0.1);
        wonTitle.setPosition(80, 150);

        Image Ecap = new Image("E-button.png"); // press E to sacrifice 1HP for 1 bullet

        Sprite playBtn = new Sprite();
        playBtn.setImage("PlayButton.png");
        playBtn.setPosition(200, 250);

        Sprite replayBtn = new Sprite();
        replayBtn.setImage("ReplayButton.png");
        replayBtn.setPosition(200, 290);

        Sprite helpBtn = new Sprite();
        helpBtn.setImage("helpButton.png");
        helpBtn.setPosition(200, 360);

        Sprite closeBtn = new Sprite();
        closeBtn.setImage("CloseButton.png");
        closeBtn.setPosition(200, 460);

        Sprite mainBtn = new Sprite();
        mainBtn.setImage("mainMenuButton.png");

        Sprite help = new Sprite();
        help.setImage("help.png");
        help.setPosition(0, 0);

        //Explode images for animation
        Image[] explode = new Image[3];
        for (int i = 0; i < 3; i++) {
            explode[i] = new Image("explode" + (i + 1) + ".png");
        }

        //SPACESHIP
        Sprite spaceShip = new Sprite();
        Image[] hitSpaceShip = new Image[3];
        for (int i = 0; i < 3; i++) {
            hitSpaceShip[i] = new Image("f-hit" + (i + 1) + ".png");
        }
        Image[] tempSpaceShip = new Image[4];
        for (int i = 0; i < 4; i++) {
            tempSpaceShip[i] = new Image("f" + (i + 1) + ".png");
        }
        spaceShip.setFrames(tempSpaceShip, 0.1);
        spaceShip.setPosition(300, 500);
        spaceShip.setVelocity(0, 0);
        spaceShip.setHP(3);
        spaceShip.setBCapacity(5);

        //BULLETS
        ArrayList<Sprite> bullets = new ArrayList<>();
        DoubleValue bulletDelay = new DoubleValue(0); //for delay between bullets
        DoubleValue convertDelay = new DoubleValue(0); //delay 1 second between each E pressed

        //ENEMY BULLETS
        ArrayList<Sprite> eBullets = new ArrayList<>();
        DoubleValue eBulletDelay = new DoubleValue(3); //for starting point and delay between enemy's bullets

        //ENEMIES
        ArrayList<Sprite> enemyList = new ArrayList<>();
        BooleanValue allReady = new BooleanValue(false);

        //Default Animation
        Image[] tempEnemy = new Image[6];
        for (int j = 0; j < 6; j++) {
            tempEnemy[j] = new Image("e1" + (j + 1) + ".png");
        }

        //Animation when getting hit by bullet
        Image[] hitEnemy = new Image[2];
        for (int j = 0; j < 2; j++) {
            hitEnemy[j] = new Image("e-hit" + (j + 1) + ".png");
        }

        //Apply Default Animation for enemy
        DoubleValue posX = new DoubleValue(150); //enemies
        DoubleValue posY = new DoubleValue(-200);
        for (int i = 0; i < 7; i++) {
            if (posX.value > 550) {
                posX.setDoubleValue(posX.value - 550);
                posY.setDoubleValue(posY.value + 100);
            }
            Sprite enemy = new Sprite();

            enemy.setFrames(tempEnemy, 0.1);
            enemy.setPosition(posX.value, posY.value);
            enemy.setHP(3);
            enemyList.add(enemy);
            posX.setDoubleValue(posX.value + 150);
        }
        //LOOT
        ArrayList<Sprite> lootList = new ArrayList<>();

        //BOSS
        BooleanValue bossExistence = new BooleanValue(false);
        ArrayList<Sprite> boss = new ArrayList<>();
        DoubleValue bossState = new DoubleValue(0);

        //Boss moves and their cooldown*****
        //Fast roll
        DoubleValue fastRollDuration = new DoubleValue(0);
        DoubleValue fastRollCoolDown = new DoubleValue(0);
        //Energy Blast
        DoubleValue blastDuration = new DoubleValue(0);
        DoubleValue blastCoolDown = new DoubleValue(0);
        ArrayList<Sprite> energyBlast = new ArrayList<>();
        Sprite charge = new Sprite();
        Sprite blast = new Sprite();
        Image[] chargeFrame = new Image[6];
        Image[] blastFrame = new Image[5];
        Image[] shieldIniFrame = new Image[5];

        //Boss shield (Phase 2)
        Sprite shield = new Sprite();
        Image[] shieldFrame = new Image[7];
        for (int i = 0; i < 7; i++) {
            shieldFrame[i] = new Image("shield1" + (i + 1) + ".png");
        }
        shield.setFrames(shieldFrame, 0.1);
        for (int i = 0; i < 6; i++) {
            chargeFrame[i] = new Image("charge" + (i + 1) + ".png");
        }
        for (int i = 0; i < 5; i++) {
            blastFrame[i] = new Image("blast" + (i + 1) + ".png");
        }
        for (int i = 0; i < 5; i++) {
            shieldIniFrame[i] = new Image("shieldIni" + (i + 1) + ".png");
        }
        charge.setFrames(chargeFrame, 0.1);
        blast.setFrames(blastFrame, 0.1);
        blast.setHeight(580);
        blast.setWidth(125);

        //for head
        Sprite bossHead = new Sprite();
        Image[] headFrames = new Image[6];

        for (int i = 0; i < 6; i++) {
            headFrames[i] = new Image("head1" + (i + 1) + ".png");
        }
        Image[] headCFrames = new Image[2];
        for (int i = 0; i < 2; i++) {
            headCFrames[i] = new Image("headCharge" + (i + 1) + ".png");
        }
        Image[] headHitFrames = new Image[2];
        for (int i = 0; i < 2; i++) {
            headHitFrames[i] = new Image("headHit.png");
        }
        Image[] headDeadFrames = new Image[5];
        for (int i = 0; i < 5; i++) {
            headDeadFrames[i] = new Image("HeadDeath" + (i + 1) + ".png");
        }
        bossHead.setFrames(headFrames, 0.1);
        bossHead.setHP(4);
        boss.add(bossHead);

        //for ring
        Sprite bossRing = new Sprite();
        Image[] ringFrames = new Image[7];
        for (int i = 0; i < 7; i++) {
            ringFrames[i] = new Image("ring1" + (i + 1) + ".png");
        }
        Image[] ringHitFrames = new Image[2];
        for (int i = 0; i < 2; i++) {
            ringHitFrames[i] = new Image("ringHit.png");
        }
        Image[] ringDeadFrames = new Image[5];
        for (int i = 0; i < 5; i++) {
            ringDeadFrames[i] = new Image("RingDeath" + (i + 1) + ".png");
        }
        bossRing.setFrames(ringFrames, 0.1);
        bossRing.setHP(4);
        boss.add(bossRing);

        //Meteor
        ArrayList<Sprite> meteorList = new ArrayList<>();
        DoubleValue meteorDelay = new DoubleValue(0);
        DoubleValue loop = new DoubleValue(0);

        //**SETUP KEY EVENTS
        ArrayList<String> input = new ArrayList<>();
        scene.setOnKeyPressed((KeyEvent e) -> {
            String code = e.getCode().toString();
            if (!input.contains(code)) {
                input.add(code);
            }
        });
        scene.setOnKeyReleased((KeyEvent e) -> {
            String code = e.getCode().toString();
            input.remove(code);
        });

        //**SETUP MOUSE CLICKED EVENTS
        scene.setOnMouseClicked((MouseEvent e) -> {
            if (gameStatus.value == 0) {
                if (playBtn.getBoundary().contains(e.getX(), e.getY())) {
                    gameStatus.setValue(1);
                }
                if (helpBtn.getBoundary().contains(e.getX(), e.getY())) {
                    gameStatus.setValue(3);
                }
                if (closeBtn.getBoundary().contains(e.getX(), e.getY())) {
                    //END GAME**
                    primaryStage.close();
                }

            }
            if (gameStatus.value == 2) {
                if (replayBtn.getBoundary().contains(e.getX(), e.getY())
                        || mainBtn.getBoundary().contains(e.getX(), e.getY())) { 
                    //reset everything to replay
                    if (replayBtn.getBoundary().contains(e.getX(), e.getY())) {
                        gameStatus.setValue(1);
                    }
                    if (mainBtn.getBoundary().contains(e.getX(), e.getY())) {
                        gameStatus.setValue(0);
                    }
                    //Reset phase
                    phase.setValue(0);

                    //Reset spaceship
                    spaceShip.setPosition(300, 500);
                    spaceShip.setVelocity(0, 0);
                    spaceShip.setDeathDuration(0);
                    spaceShip.setHP(3);
                    spaceShip.setBCapacity(5);
                    lootList.clear();

                    //Reset enemy mobs
                    enemyList.clear();
                    eBullets.clear();
                    round.setValue(0);
                    allReady.setValue(false);
                    posX.setDoubleValue(150); //enemies
                    posY.setDoubleValue(-200);
                    for (int i = 0; i < 7; i++) {
                        if (posX.value > 550) {
                            posX.setDoubleValue(posX.value - 550);
                            posY.setDoubleValue(posY.value + 100);
                        }
                        Sprite enemy = new Sprite();

                        enemy.setFrames(tempEnemy, 0.1);
                        enemy.setPosition(posX.value, posY.value);
                        enemy.setHP(3);
                        enemyList.add(enemy);
                        posX.setDoubleValue(posX.value + 150);
                    }

                    //Reset boss
                    boss.clear();
                    bossState.setDoubleValue(0);
                    bossHead.setHP(4);
                    bossHead.setDeathDuration(0);
                    boss.add(bossHead);
                    bossRing.setHP(4);
                    bossRing.setDeathDuration(0);
                    boss.add(bossRing);
                    boss.get(0).setFrames(headFrames, 0.1);
                    boss.get(1).setFrames(ringFrames, 0.1);
                    boss.get(0).setPrepare(true);
                    boss.get(1).setPrepare(true);
                    bossExistence.setValue(false);
                    energyBlast.clear();
                    meteorList.clear();

                }
                if (closeBtn.getBoundary().contains(e.getX(), e.getY())) {
                    //End Game****
                    primaryStage.close();
                }
            }
            if (gameStatus.value == 3) {
                if (mainBtn.getBoundary().contains(e.getX(), e.getY())) {
                    gameStatus.setValue(0);
                }
            }
        });

        //GAME LOOP******
        long startNanoTime = System.nanoTime();
        LongValue lastNanoTime = new LongValue(System.nanoTime());
        new AnimationTimer() { //Called continuously at a rate of 30-60fps.
            @Override
            public void handle(long currentNanoTime) {
                //Create variables to keep track of game time
                double t = (currentNanoTime - startNanoTime) / 1000000000.00;
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.setValue(currentNanoTime);

                //**USER INPUT
                if (gameStatus.value == 1) {
                    spaceShip.setVelocity(0, 0);
                    //Set spaceship's speed in all direction
                    if (spaceShip.getHP() > 0) {
                        if ((input.contains("RIGHT") || input.contains("D")) && (spaceShip.getPositionX() < (scene.getWidth() - spaceShip.getWidth()))) {
                            spaceShip.addVelocity(250, 0);
                        }
                        if ((input.contains("LEFT") || input.contains("A")) && (spaceShip.getPositionX() > 0)) {

                            spaceShip.addVelocity(-250, 0);
                        }
                        if ((input.contains("UP") || input.contains("W")) && (spaceShip.getPositionY() > 0)) {
                            spaceShip.addVelocity(0, -250);
                        }
                        if ((input.contains("DOWN") || input.contains("S")) && (spaceShip.getPositionY() < (scene.getHeight() - spaceShip.getHeight()))) {
                            spaceShip.addVelocity(0, 250);
                        }
                    }

                    if ((input.contains("E") && (spaceShip.getHP() > 1))) {
                        if (t - convertDelay.value >= 1) {
                            spaceShip.setHP(spaceShip.getHP() - 1);
                            spaceShip.setBCapacity(spaceShip.getBCapacity() + 1);
                            convertDelay.setDoubleValue(t);
                        }
                    }

                    //**UPDATE OBJECT STATUS
                    //SPACESHIP
                    //Switch animation when player get hit
                    if (spaceShip.getHP() > 0) {
                        if ((spaceShip.getHitDuration() != 0) && (t - spaceShip.getHitDuration() <= 0.3)) {
                            spaceShip.setFrames(hitSpaceShip, 0.2);
                        } else {
                            spaceShip.setFrames(tempSpaceShip, 0.1);
                        }
                    }
                    //Switch to exploding animation when HP reaches 0.
                    if (spaceShip.getHP() <= 0 && spaceShip.getDeathDuration() == 0) {
                        spaceShip.setVelocity(0, 0);
                        spaceShip.setFrames(explode, 0.1);
                        spaceShip.setDeathDuration(t);
                    }
                    //Show result after 0.7second exploding
                    if (spaceShip.getDeathDuration() != 0 && t - spaceShip.getDeathDuration() > 1) {
                        gameStatus.setValue(2);
                        result.setValue(2);
                    }

                    //BULLETS
                    if (input.contains("SPACE") && (t - bulletDelay.value >= 0.5)
                            && (spaceShip.getBCapacity() > 0)) { // one-second delay between bullets
                        Sprite bullet = new Sprite();
                        Image[] bulletArray = new Image[4];
                        for (int i = 0; i < 4; i++) {
                            bulletArray[i] = new Image("bullet_" + (i + 1) + ".png");
                        }
                        bullet.setFrames(bulletArray, 0.1);
                        bullet.setPosition(spaceShip.getPositionX() + spaceShip.getWidth() / 2, spaceShip.getPositionY());
                        bullet.setVelocity(0, -350);
                        bullet.setHP(1);
                        bullets.add(bullet);
                        bulletDelay.setDoubleValue(t); //for delay between bullets
                        spaceShip.setBCapacity(spaceShip.getBCapacity() - 1); //substract bullet capacity of spaceship
                    }
                    Iterator<Sprite> bulletIter = bullets.iterator();
                    while (bulletIter.hasNext()) {
                        Sprite bullet = bulletIter.next();
                        if (bullet.getPositionY() <= 20) { //Bullets disappear at borders.
                            bullet.setHP(0);
                        }
                        if (bullet.getHP() == 0) {
                            bulletIter.remove();
                        }
                    }
                    //LOOT MODIFICATION
                    for (Sprite loot : lootList) {
                        if (loot.intersects(spaceShip)) {
                            loot.setHP(0);
                            spaceShip.setHP(spaceShip.getHP() + 1);
                            spaceShip.setBCapacity(spaceShip.getBCapacity() + 3);
                        }
                    }
                    Iterator<Sprite> lootIter = lootList.iterator();
                    while (lootIter.hasNext()) {
                        Sprite loot = lootIter.next();
                        if (loot.getPositionY() >= scene.getHeight() - 20) { //Bullets disappear at borders.
                            lootIter.remove();
                        }
                        if (loot.getHP() == 0) {
                            lootIter.remove();
                        }
                    }
                    //ENEMIES + BOSSES
                    if (phase.value == 0) { //Before boss
                        //ENEMIES
                        if (allReady.value == false) {
                            int count = 0;
                            for (Sprite enemy : enemyList) {
                                if (enemy.getPrepare() == false) {
                                    count++;
                                }
                                //can only reach this statement when all enemies are done preparing
                                if (count == enemyList.size()) {
                                    allReady.setValue(true);
                                }
                            }
                        }
                        if (!enemyList.isEmpty()
                                && enemyList.get(enemyList.size() - 1).getPositionY() < posY.value + 300
                                && allReady.value == false) {
                            for (int i = 0; i < enemyList.size(); i++) {
                                enemyList.get(i).setVelocity(0, 100);
                            }
                        } else {
                            for (Sprite enemy : enemyList) {
                                if (enemy.getPrepare() == true) { //switch animation from prepare phase to ready phase
                                    enemy.setVelocity(150, 0);
                                    enemy.setPrepare(false);
                                }
                                if ((enemy.getPositionX() >= (scene.getWidth() - enemy.getWidth() - 3))) {
                                    enemy.setVelocity(-150, 0);
                                } else if (enemy.getPositionX() <= 3) {
                                    enemy.setVelocity(150, 0);
                                }
                                if (enemy.intersects(spaceShip)) {
                                    enemy.setHP(0);
                                    spaceShip.setHP(0);
                                }
                                for (Sprite bullet : bullets) {
                                    if (enemy.intersects(bullet)) {
                                        enemy.setHitDuration(t);
                                        enemy.setHP(enemy.getHP() - 1);
                                        bullet.setHP(0);
                                    }
                                }
                                if (enemy.getHP() > 0) {
                                    //Switch animation when enemy got hit
                                    if ((enemy.getHitDuration() != 0) && (t - enemy.getHitDuration() <= 0.3)) {
                                        enemy.setFrames(hitEnemy, 0.2);
                                    } else {
                                        enemy.setFrames(tempEnemy, 0.1);
                                    }
                                }
                                //Switch to exploding animation when HP reaches 0.
                                if (enemy.getHP() <= 0 && enemy.getDeathDuration() == 0) {
                                    enemy.setVelocity(0, 0);
                                    enemy.setFrames(explode, 0.1);
                                    enemy.setDeathDuration(t);
                                }
                            }
                        }

                        Iterator<Sprite> enemyIter = enemyList.iterator();
                        while (enemyIter.hasNext()) {
                            Sprite enemy = enemyIter.next();
                            if (spaceShip.getHP() > 0
                                    && enemy.getDeathDuration() != 0
                                    && t - enemy.getDeathDuration() > 0.3) {
                                enemyIter.remove();
                                //Drop Loot
                                Sprite loot = new Sprite();
                                Image[] lootArray = new Image[5];
                                for (int i = 0; i < 5; i++) {
                                    lootArray[i] = new Image("Loot" + (i) + ".png");
                                }
                                loot.setFrames(lootArray, 0.1);
                                loot.setPosition(enemy.getPositionX(), enemy.getPositionY());
                                loot.setVelocity(0, 50);
                                loot.setHP(1);
                                lootList.add(loot);
                            }
                        }
                        if (enemyList.isEmpty()) {
                            int add = round.value + 1;
                            if (round.value < 0) { //round of mobs
                                allReady.setValue(false);
                                posX.setDoubleValue(150); //enemies
                                posY.setDoubleValue(-200);
                                for (int i = 0; i < 7 + add; i++) { //# of enemies based on round
                                    if (posX.value > 550) {
                                        posX.setDoubleValue(posX.value - 550);
                                        posY.setDoubleValue(posY.value + 100);
                                    }
                                    Sprite enemy = new Sprite();

                                    enemy.setFrames(tempEnemy, 0.1);
                                    enemy.setPosition(posX.value, posY.value);
                                    enemy.setHP(3);
                                    enemyList.add(enemy);
                                    posX.setDoubleValue(posX.value + 150);
                                    round.setValue(round.value + 1);
                                }
                            } else {
                                phase.setValue(1);
                            }
                        }
                    }
                    // ENEMIES'BULLETS
                    if ((t - eBulletDelay.value) >= 1) { //delay between enemy's bullets
                        for (Sprite enemy : enemyList) {
                            if (enemy.getPrepare() == false) {
                                Sprite ebullet2 = new Sprite();
                                Image[] bulletArray = new Image[4];
                                for (int i = 0; i < 4; i++) {
                                    bulletArray[i] = new Image("bullet_" + (i + 1) + ".png");
                                }
                                ebullet2.setFrames(bulletArray, 0.1);
                                ebullet2.setPosition(enemy.getPositionX(), enemy.getPositionY());
                                ebullet2.setVelocity(0, 350);
                                ebullet2.setHP(1);
                                eBullets.add(ebullet2);
                            }
                        }
                        eBulletDelay.setDoubleValue(t);
                    }
                    for (Sprite eBullet : eBullets) {
                        if (eBullet.intersects(spaceShip)) {
                            spaceShip.setHP(spaceShip.getHP() - 1);
                            spaceShip.setHitDuration(t);
                            eBullet.setHP(0);
                        }
                        if (eBullet.getPositionY() >= scene.getHeight() - 20) { //Bullets disappear at borders.
                            eBullet.setHP(0);
                        }
                    }
                    Iterator<Sprite> eBulletIter = eBullets.iterator();
                    while (eBulletIter.hasNext()) {
                        Sprite eBullet = eBulletIter.next();

                        if (eBullet.getHP() == 0) {
                            eBulletIter.remove();
                        }
                    }

                    //BOSS
                    if (phase.value == 1) {
                        if (bossExistence.value == false) {
                            shield.setPosition(-500, -500);
                            boss.get(0).setPosition(240, -220);
                            boss.get(0).setVelocity(0, 50);
                            bossExistence.setValue(true);
                            meteorDelay.setDoubleValue(t);
                            fastRollCoolDown.setDoubleValue(t);
                        }

                        //set position and speed of boss ring based on head
                        if (boss.size() == 2) {
                            if (boss.get(1).getHP() > 0) {
                                boss.get(1).setPosition(boss.get(0).getPositionX() - 82, boss.get(0).getPositionY() - 82);
                                if (bossState.value == 0) {
                                    if (boss.get(0).getPositionY() >= 120 && boss.get(0).getPrepare() == true) {
                                        boss.get(0).setVelocity(180, 0);
                                        boss.get(0).setPrepare(false);
                                        boss.get(1).setPrepare(false);
                                    } else {
                                        if (boss.get(0).getPrepare() == false) {
                                            //boss bounces back to player's position when reaching walls
                                            if (boss.get(1).getPositionX() <= 0
                                                    || boss.get(1).getPositionX() >= (scene.getWidth() - boss.get(1).getWidth())
                                                    || boss.get(1).getPositionY() <= 0
                                                    || boss.get(1).getPositionY() >= (scene.getHeight() - boss.get(1).getHeight())) {
                                                //Calculate unit-vector to determine bouncing direction
                                                double a = (spaceShip.getPositionX() - boss.get(0).getPositionX());
                                                double b = (spaceShip.getPositionY() - boss.get(0).getPositionY());
                                                //multiply for a constance to get speed in decided direction
                                                boss.get(0).setVelocity((180 * a / sqrt(a * a + b * b)), 180 * b / (sqrt(a * a + b * b)));
                                            }
                                        }
                                    }
                                    if (t - fastRollCoolDown.value >= 15) {
                                        bossState.setDoubleValue(1);
                                        fastRollDuration.setDoubleValue(t);
                                        boss.get(1).setFrames(ringFrames, 0.05);
                                        boss.get(0).setFrames(headCFrames, 0.05);
                                        boss.get(0).setVelocity(0, 0);
                                    }
                                }
                                if (bossState.value == 1) {
                                    if (t - fastRollDuration.value >= 2) {
                                        if (t - fastRollDuration.value < 2.1) {
                                            double a = (spaceShip.getPositionX() - boss.get(0).getPositionX());
                                            double b = (spaceShip.getPositionY() - boss.get(0).getPositionY());
                                            //multiply for a constance to get speed in decided direction
                                            boss.get(0).setVelocity((450 * a / sqrt(a * a + b * b)), 450 * b / (sqrt(a * a + b * b)));
                                        } else if (t - fastRollDuration.value < 4) {
                                            if (boss.get(0).getPositionX() <= 0
                                                    || boss.get(0).getPositionX() >= (scene.getWidth() - boss.get(0).getWidth())
                                                    || boss.get(0).getPositionY() <= 0
                                                    || boss.get(0).getPositionY() >= (scene.getHeight() - boss.get(0).getHeight())) {
                                                boss.get(0).setVelocity(0, 0);
                                            }
                                        } else {
                                            boss.get(1).setFrames(ringFrames, 0.1);
                                            boss.get(0).setFrames(headFrames, 0.1);
                                            fastRollCoolDown.setDoubleValue(t);
                                            bossState.setDoubleValue(0);
                                        }
                                    }
                                }
                                if (boss.get(0).getPrepare() == false && t - meteorDelay.value >= 15) {
                                    meteorDelay.setDoubleValue(t);
                                    Sprite meteor = new Sprite();
                                    Image[] meteorFrames = new Image[7];
                                    for (int i = 0; i < 7; i++) {
                                        meteorFrames[i] = new Image("meteor" + (i + 1) + ".png");
                                    }
                                    meteor.setFrames(meteorFrames, 0.1);
                                    meteor.setPosition(Math.random() * (600 - meteor.getWidth()), -20);
                                    meteor.setHP(1);
                                    meteor.setVelocity(0, 50);
                                    meteorList.add(meteor);
                                }

                                //For ring(circle) hitbox
                                if (boss.get(1).circleIntersect(spaceShip)) {
                                    spaceShip.setHP(0);
                                }

                                for (Sprite meteor : meteorList) {
                                    if (meteor.intersects(boss.get(1))) {
                                        meteor.setHP(0);
                                        boss.get(1).setHP(boss.get(1).getHP() - 1);
                                        boss.get(1).setHitDuration(t);
                                    }
                                    if (meteor.intersects(spaceShip)) {
                                        meteor.setHP(0);
                                        spaceShip.setHP(0);
                                    }
                                }

                                if (boss.get(1).getHitDuration() != 0) {
                                    if (t - boss.get(1).getHitDuration() <= 0.3) {
                                        boss.get(1).setFrames(ringHitFrames);
                                    } else if (t - boss.get(1).getHitDuration() <= 0.6) {
                                        boss.get(1).setFrames(ringFrames);
                                    }
                                }
                            } else {
                                if (boss.get(1).getDeathDuration() == 0) {
                                    boss.get(1).setFrames(ringDeadFrames, 0.1);
                                    boss.get(0).setVelocity(0, 0);
                                    boss.get(1).setDeathDuration(t);
                                }
                                //Show result after 2 second exploding
                                if (boss.get(1).getDeathDuration() != 0 && t - boss.get(1).getDeathDuration() > 3) {
                                    boss.get(1).setPrepare(true);
                                }
                            }
                            enemyList.clear();

                            //Boss: Phase 2
                        } else if (boss.size() == 1) {

                            //Set up for boss fight phase 2
                            if (boss.get(0).getHP() > 0) {
                                if (((boss.get(0).getPositionY() < 50 || boss.get(0).getPositionY() > 70))) {
                                    double a = (260 - boss.get(0).getPositionX());
                                    double b = (60 - boss.get(0).getPositionY());
                                    boss.get(0).setVelocity((220 * a / sqrt(a * a + b * b)), 220 * b / (sqrt(a * a + b * b)));
                                } else {

                                    if (enemyList.isEmpty()) {
                                        boss.get(0).setFrames(headCFrames, 0.05);
                                        shield.setFrames(shieldIniFrame, 0.1);
                                        shield.setPosition(boss.get(0).getPositionX() - 12,
                                                boss.get(0).getPositionY() + 40);
                                        boss.get(0).setVelocity(0, 0);
                                        boss.get(0).setPrepare(true);
                                        allReady.setValue(false);
                                        for (int i = 0; i < 3; i++) {
                                            //Create 3 mobs around boss
                                            if (posX.value > 550) {
                                                posX.setDoubleValue(posX.value - 550);
                                                posY.setDoubleValue(posY.value + 100);
                                            }
                                            Sprite enemy = new Sprite();

                                            enemy.setFrames(tempEnemy, 0.1);
                                            enemy.setPosition(boss.get(0).getPositionX() + 35, -200);
                                            enemy.setVelocity(0, 200);
                                            enemy.setHP(3);
                                            enemyList.add(enemy);
                                        }
                                    }
                                    double count = 0;
                                    if (allReady.value == false) {
                                        for (Sprite enemy : enemyList) {
                                            if (enemy.getPrepare() == true) {
                                                if (enemy.getPositionY()
                                                        >= boss.get(0).getPositionY() - enemy.getHeight() - 20) {
                                                    enemy.setVelocity(0, 0);
                                                    count++;

                                                }
                                            }
                                        }
                                        if (count == 3) {
                                            loop.setDoubleValue(t);
                                            allReady.setValue(true);
                                        }
                                    } else { //AllReady is true
                                        if (t - loop.value >= 1.7) {
                                            for (Sprite enemy : enemyList) {
                                                if (enemy.getPrepare() == true) {
                                                    enemy.setPrepare(false);
                                                    loop.setDoubleValue(t);
                                                    enemy.setMoveDuration(t);
                                                    break;
                                                }
                                            }
                                        }
                                        if (boss.get(0).getPrepare() == true) {
                                            if (t - loop.value >= 2) {
                                                boss.get(0).setVelocity(150, 0);
                                                boss.get(0).setPrepare(false);
                                                blastCoolDown.setDoubleValue(t);
                                                bossState.setDoubleValue(0);

                                            }
                                        } else { //boss prepare is false, code all boss moves here
                                            if (boss.get(0).getPositionX() + boss.get(0).getWidth() >= 550) {
                                                boss.get(0).setVelocity(-120, 0);
                                            }
                                            if (boss.get(0).getPositionX() <= 30) {
                                                boss.get(0).setVelocity(120, 0);
                                            }
                                            if (bossState.value == 0) {
                                                if (shield.getFrames() == shieldIniFrame) {
                                                    shield.setFrames(shieldFrame, 0.1);
                                                    boss.get(0).setFrames(headFrames, 0.1);
                                                }
                                                shield.setPosition(boss.get(0).getPositionX() - 55,
                                                        boss.get(0).getPositionY() - 70);
                                                if (t - blastCoolDown.value >= 10) {
                                                    bossState.setDoubleValue(1);
                                                    blastDuration.setDoubleValue(t);
                                                    energyBlast.add(charge);
                                                    shield.setPosition(-500, -500);
                                                    boss.get(0).setFrames(headCFrames, 0.05);
                                                }
                                            }
                                            if (bossState.value == 1) {
                                                energyBlast.get(0).setPosition(
                                                        boss.get(0).getPositionX() - 42,
                                                        boss.get(0).getPositionY() + 8);
                                                if (t - blastDuration.value >= 3) {
                                                    if (energyBlast.size() == 1) {
                                                        energyBlast.add(blast);
                                                    }
                                                    if (t - blastDuration.value <= 5) {
                                                        energyBlast.get(1).setPosition(
                                                                boss.get(0).getPositionX() - 15,
                                                                boss.get(0).getPositionY() + 55);
                                                    } else {
                                                        blastCoolDown.setDoubleValue(t);
                                                        bossState.setDoubleValue(0);
                                                        boss.get(0).setFrames(headFrames, 0.1);
                                                        energyBlast.clear();

                                                    }
                                                }
                                            }
                                        }
                                        if (boss.get(0).getHP() > 0) {
                                            if (boss.get(0).getHitDuration() != 0) {
                                                if (t - boss.get(0).getHitDuration() <= 0.3) {
                                                    boss.get(0).setFrames(headHitFrames, 0.2);
                                                } else if (t - boss.get(0).getHitDuration() <= 0.6) {
                                                    boss.get(0).setFrames(headCFrames, 0.1);
                                                }
                                            }
                                        }
                                        for (Sprite enemy : enemyList) {
                                            if (enemy.getPrepare() == false) {

                                                double a = t - enemy.getMoveDuration();
                                                enemy.setVelocity(boss.get(0).getVelocityX() + 121 * Math.cos(1.2 * a),
                                                        121 * Math.sin(1.2 * a));
                                            }
                                        }

                                    }
                                }
                            } else {
                                if (boss.get(0).getDeathDuration() == 0) {
                                    boss.get(0).setFrames(headDeadFrames, 0.1);
                                    boss.get(0).setVelocity(0, 0);
                                    boss.get(0).setDeathDuration(t);
                                    enemyList.clear();
                                    energyBlast.clear();
                                }
                                //Show result after 2 second exploding
                                if (boss.get(0).getDeathDuration() != 0 && t - boss.get(0).getDeathDuration() > 3) {
                                    boss.get(0).setPrepare(true);
                                }
                            }
                        }
                        //Bullet Collision
                        for (Sprite bullet : bullets) {
                            if (boss.size() == 2 && bullet.intersects(boss.get(1))) {
                                bullet.setHP(0);
                            }
                            if (boss.size() == 1 && bullet.intersects(boss.get(0))) {
                                bullet.setHP(0);
                                if (boss.get(0).getPrepare() == false) {
                                    boss.get(0).setHP(boss.get(0).getHP() - 1);
                                    boss.get(0).setHitDuration(t);
                                }
                            }
                            for (Sprite meteor : meteorList) {
                                if (bullet.intersects(meteor)) {
                                    bullet.setHP(0);
                                }
                            }
                            if (bullet.intersects(shield)) {
                                bullet.setHP(0);
                            }
                        }
                        for (Sprite blast : energyBlast) {
                            if (blast.intersects(spaceShip)) {
                                spaceShip.setHP(0);
                            }
                        }
                        if (shield.intersects(spaceShip)) {
                            spaceShip.setHP(0);
                        }
                        if (boss.get(0).intersects(spaceShip)) {
                            spaceShip.setHP(0);
                        }

                        Iterator<Sprite> bossIter = boss.iterator();
                        while (bossIter.hasNext()) {
                            Sprite bossPart = bossIter.next();
                            if (bossPart.getHP() == 0 && (bossPart.prepare)) {
                                bossIter.remove();
                            }
                        }
                        Iterator<Sprite> meteorIter = meteorList.iterator();
                        while (meteorIter.hasNext()) {
                            Sprite meteor = meteorIter.next();
                            if (meteor.getPositionY() >= scene.getHeight() - 20) { //Bullets disappear at borders.
                                meteorIter.remove();
                            }
                            if (meteor.getHP() == 0) {
                                meteorIter.remove();
                            }
                        }
                        if (boss.isEmpty()) {
                            gameStatus.setValue(2);
                            result.setValue(1);
                        }
                    }
                }

                //Update Objects
                spaceShip.update(elapsedTime);
                for (Sprite bullet : bullets) {
                    bullet.update(elapsedTime);
                }
                for (Sprite enemy : enemyList) {
                    enemy.update(elapsedTime);
                }
                for (Sprite loot : lootList) {
                    loot.update(elapsedTime);
                }
                for (Sprite eBullet : eBullets) {
                    eBullet.update(elapsedTime);
                }
                for (Sprite temp : boss) {
                    temp.update(elapsedTime);
                }
                for (Sprite meteor : meteorList) {
                    meteor.update(elapsedTime);
                }
                for (Sprite blast : energyBlast) {
                    blast.update(elapsedTime);
                }
                shield.update(elapsedTime);

                //RENDER****** (To render the images into the buffer)
                gc.clearRect(0, 0, 600, 600);
                background.renderFrame(gc, t);
                switch (gameStatus.value) { //to render based on game status
                    case 0: //Start Menu
                        title.renderFrame(gc, t);
                        playBtn.render(gc);
                        helpBtn.render(gc);
                        closeBtn.render(gc);
                        gc.setFill(Color.WHITE);
                        gc.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
                        gc.fillText("Produced by: Thong Nguyen", 20, 570);
                        break;
                    case 1: //In-game
                        for (Sprite bullet : bullets) {
                            bullet.renderFrame(gc, t);
                        }
                        for (Sprite loot : lootList) {
                            loot.renderFrame(gc, t);
                        }
                        for (Sprite eBullet : eBullets) {
                            eBullet.renderFrame(gc, t);
                        }
                        if (phase.value == 0) { // render mob enemies
                            for (Sprite enemy : enemyList) {
                                enemy.renderFrame(gc, t);
                            }
                        }
                        if (phase.value == 1) { // render boss
                            for (Sprite e : boss) {
                                e.renderFrame(gc, t);
                            }
                            for (Sprite enemy : enemyList) {
                                enemy.renderFrame(gc, t);
                            }
                            for (Sprite meteor : meteorList) {
                                meteor.renderFrame(gc, t);
                            }
                            for (Sprite blast : energyBlast) {
                                blast.renderFrame(gc, t);
                            }
                            shield.renderFrame(gc, t);
                        }
                        spaceShip.renderFrame(gc, t); //render player ship

                        //draw in-game text and buttons
                        gc.setFill(Color.RED);
                        gc.setStroke(Color.WHITE);
                        gc.setLineWidth(0.5);
                        gc.setFont(Font.font("Times New Roman", 25));
                        gc.fillText("HP: " + (int) spaceShip.getHP(), 30, 50);
                        gc.strokeText("HP: " + (int) spaceShip.getHP(), 30, 50);
                        gc.setFill(Color.GREEN);
                        gc.fillText("Bullets: " + (int) spaceShip.getBCapacity(), 30, 80);
                        gc.strokeText("Bullets: " + (int) spaceShip.getBCapacity(), 30, 80);
                        gc.drawImage(Ecap, 15, 560, 30, 30);
                        gc.setFill(Color.WHITE);
                        gc.setFont(Font.font(20));
                        gc.fillText(": HP -> Bullet", 55, 585);
                        break;
                    case 2: //End game
                        if (result.value == 1) {
                            wonTitle.renderFrame(gc, t);
                        } else if (result.value == 2) {
                            lostTitle.renderFrame(gc, t);
                        }
                        replayBtn.render(gc);
                        mainBtn.setPosition(200, 375);
                        mainBtn.render(gc);
                        closeBtn.render(gc);
                        break;
                    case 3:
                        help.render(gc);
                        mainBtn.setPosition(400, 15);
                        mainBtn.render(gc);
                        break;
                    default:
                        break;
                }
            }
        }
                .start();

        primaryStage.setScene(scene);
        primaryStage.show(); //draw images from buffer to the screen
    }

    //BASIC CLASS FOR GAME OBJECTS 
    public class Sprite {

        private Image image;
        private Image[] frames;
        private double duration;
        private double positionX;
        private double positionY;
        private double velocityX;
        private double velocityY;
        private double width;
        private double height;
        private double hp;
        private double hitDuration = 0;
        private int bCapacity;
        private boolean prepare = true; //appearing animation
        private double deathDuration = 0;
        private double moveDuration = 0;

        //Series of get-set methods
        public void setPrepare(boolean val) {
            prepare = val;
        }

        public boolean getPrepare() {
            return prepare;
        }

        //Apply animations to object
        public void setFrames(Image[] framesArray, double dur) {
            frames = framesArray;
            duration = dur;
            width = framesArray[0].getWidth();
            height = framesArray[0].getHeight();
        }

        public void setFrames(Image[] framesArray) {
            frames = framesArray;
            width = framesArray[0].getWidth();
            height = framesArray[0].getHeight();
        }

        public Image[] getFrames() {
            return frames;
        }

        public Image getFrame(double time) {
            int index = (int) ((time % (frames.length * duration)) / duration);
            return frames[index];
        }

        public void setHitDuration(double value) {
            hitDuration = value;
        }

        public double getHitDuration() {
            return hitDuration;
        }

        //Get width and height
        public double getWidth() {
            return width;
        }

        public void setWidth(double value) {
            width = value;
        }

        public double getHeight() {
            return height;
        }

        public void setHeight(double value) {
            height = value;
        }

        public void setPosition(double x, double y) {
            positionX = x;
            positionY = y;
        }

        public double getPositionX() {
            return positionX;
        }

        public double getPositionY() {
            return positionY;
        }

        public void setVelocity(double x, double y) {
            velocityX = x;
            velocityY = y;
        }

        public void addVelocity(double x, double y) {
            velocityX += x;
            velocityY += y;
        }

        public double getVelocityX() {
            return velocityX;
        }

        public double getVelocityY() {
            return velocityY;
        }

        public void setHP(double health) {
            hp = health;
        }

        public double getHP() {
            return hp;
        }

        public void setBCapacity(int value) {
            bCapacity = value;
        }

        public int getBCapacity() {
            return bCapacity;
        }

        public void setImage(Image i) {
            image = i;
            width = i.getWidth();
            height = i.getHeight();
        }

        public void setImage(String filename) {
            Image i = new Image(filename);
            image = new Image(filename);
            setImage(i);
        }

        public void setDeathDuration(double value) {
            deathDuration = value;
        }

        public double getDeathDuration() {
            return deathDuration;
        }

        public void setMoveDuration(double value) {
            moveDuration = value;
        }

        public double getMoveDuration() {
            return moveDuration;
        }

        //To update current status of object created by this class
        public void update(double time) {
            positionX += velocityX * time;
            positionY += velocityY * time;
        }

        //These 2 methods are used to render and draw images (frames)
        public void render(GraphicsContext gc) {
            gc.drawImage(image, positionX, positionY);
        }

        public void renderFrame(GraphicsContext gc, double time) {
            gc.drawImage(this.getFrame(time), positionX, positionY, width, height);
        }

        //These 2 methods are used for collision between 2 rectangular objects
        public Rectangle2D getBoundary() {
            return new Rectangle2D(positionX, positionY, width, height);
        }

        public boolean intersects(Sprite s) {
            return s.getBoundary().intersects(this.getBoundary());
        }

        //for detecting collision between circle and rectanglular objects
        public boolean circleIntersect(Sprite object) {
            Circle hitBox = new Circle(this.getWidth() / 2 - 20);
            hitBox.setCenterX(this.getPositionX() + this.getWidth() / 2);
            hitBox.setCenterY(this.getPositionY() + this.getHeight() / 2);
            double a = Math.sqrt(Math.pow(object.getWidth() / 2, 2)
                    + Math.pow(object.getHeight() / 2, 2));
            double limit = hitBox.getRadius() + a;
            double shipX = object.getPositionX() + object.getWidth() / 2;
            double shipY = object.getPositionY() + object.getHeight() / 2;
            double distance = Math.sqrt(Math.pow(shipX - hitBox.getCenterX(), 2)
                    + Math.pow(shipY - hitBox.getCenterY(), 2));

            if (distance <= limit) {
                return true;
            } else {
                return false;
            }
        }
    }

//Values created by these classes are modifiable during the gameloop using
//member methods, unlike the primitives classes.
    public class LongValue {

        public long value;

        public LongValue(long val) {
            value = val;
        }

        public void setValue(long val) {
            value = val;
        }
    }

    public class DoubleValue {

        double value;

        public DoubleValue(double val) {
            value = val;
        }

        public void setDoubleValue(double val) {
            value = val;
        }
    }

    public class IntValue {

        public int value;

        public IntValue(int val) {
            value = val;
        }

        public void setValue(int val) {
            value = val;
        }
    }

    public class BooleanValue {

        public boolean value;

        public BooleanValue(boolean val) {
            value = val;
        }

        public void setValue(boolean val) {
            value = val;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
