package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.logging.Handler;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	private Music music;
	Texture[] man;
	Texture dizzy;
	int manState = 0;
	int pause = 0;
	float gravity = 0.2f;
	float velocity = 0;
	int manY = 0;
	int score = 0;
	int gameState =0;
	Rectangle manRec;
	BitmapFont bitmapFont;

	ArrayList<Integer> coinX = new ArrayList<Integer>();
	ArrayList<Integer> coinY = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRec = new ArrayList<Rectangle>();
	Texture coin;
	int coinCount;
	ArrayList<Integer> mineX = new ArrayList<Integer>();
	ArrayList<Integer> mineY = new ArrayList<Integer>();
	ArrayList<Rectangle> mineRec = new ArrayList<Rectangle>();

	Texture mine;
	int mineCount;
	Random random = new Random();


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		dizzy = new Texture("dizzy-1.png");
		coin = new Texture("coin.png");
		mine = new Texture("bomb.png");
		manRec = new Rectangle();
		bitmapFont = new BitmapFont();
		bitmapFont.setColor(Color.BLUE);
		bitmapFont.getData().setScale(10);
		manY =Gdx.graphics.getHeight()/2 - man[manState].getHeight()/2;
		music = Gdx.audio.newMusic(Gdx.files.internal("bahr.mp3"));
		music.setLooping(true);

	}

	public void makeCoin(){
		float height = random.nextFloat() * (Gdx.graphics.getHeight()-coin.getHeight());
		coinY.add((int)height);
		coinX.add(Gdx.graphics.getWidth());
	}

	public void makeMine(){
		float height = random.nextFloat() * (Gdx.graphics.getHeight()-mine.getHeight());
		mineY.add((int)height);
		mineX.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
        batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if (gameState == 1 ){
			music.play();
			if (coinCount <100){
				coinCount++;
			}else{
				coinCount = 0;
				makeCoin();
			}
			coinRec.clear();
			for (int i = 0;i<coinX.size();i++){
				batch.draw(coin,coinX.get(i),coinY.get(i));
				coinX.set(i,coinX.get(i)-4);
				coinRec.add(new Rectangle(coinX.get(i),coinY.get(i),coin.getWidth(),coin.getHeight()));
			}
			if (mineCount < 500){
				mineCount++;
			}else {
				mineCount=0;
				makeMine();
			}
			mineRec.clear();
			for (int j =0; j<mineX.size();j++){
				batch.draw(mine,mineX.get(j),mineY.get(j));
				mineX.set(j,mineX.get(j)-6);
				mineRec.add(new Rectangle(mineX.get(j),mineY.get(j),mine.getWidth(),mine.getHeight()));

			}
			if (Gdx.input.justTouched()){
				velocity = -10;
			}
			if (pause <8){
				pause++;
			}else {
				pause=0;
				if (manState < 3) {
					manState++;
				} else {
					manState = 0;
				}
			}
			velocity = velocity + gravity;
			if (manY > 0 || Gdx.input.justTouched()){
				manY -= velocity;
			}

		}
		else if (gameState == 0){
			if (Gdx.input.justTouched()){
				gameState = 1;
				music.play();

			}
		}else if (gameState == 2){
			music.pause();
			if (Gdx.input.justTouched()){
				gameState = 1;
				score =0;
				manY = Gdx.graphics.getHeight()/2 - man[manState].getHeight()/2;
				velocity=0;
				coinX.clear();
				coinY.clear();
				coinRec.clear();
				coinCount = 0;
				mineRec.clear();
				mineY.clear();
				mineX.clear();
				mineCount=0;
			}
		}
		if (gameState == 2){
			batch.draw(dizzy,Gdx.graphics.getWidth() / 2 - man[0].getWidth() / 2, manY);
		}else {
			batch.draw(man[manState], Gdx.graphics.getWidth() / 2 - man[0].getWidth() / 2, manY);
		}
		manRec = new Rectangle(Gdx.graphics.getWidth()/2 - man[0].getWidth()/2,manY,man[manState].getWidth(),man[manState].getHeight());
		for (int i =0 ;i<coinRec.size();i++){
			if (Intersector.overlaps(manRec,coinRec.get(i))){
				score++;
				coinRec.remove(i);
				coinX.remove(i);
				coinY.remove(i);
				break;
			}
		}
		for (int i =0 ;i<mineRec.size();i++){
			if (Intersector.overlaps(manRec,mineRec.get(i))){
				gameState = 2;
			}
		}

		bitmapFont.draw(batch,String.valueOf(score),100,200);
		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
