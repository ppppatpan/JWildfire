package org.jwildfire.swing;

import java.util.concurrent.TimeUnit;
import org.jwildfire.base.Prefs;
import org.jwildfire.base.Tools;
import org.jwildfire.base.WindowPrefs;
import org.jwildfire.base.QualityProfile;
import org.jwildfire.base.ResolutionProfile;
import org.jwildfire.create.tina.randomflame.AllRandomFlameGenerator;
import org.jwildfire.create.tina.randomflame.RandomFlameGenerator;
import org.jwildfire.create.tina.randomflame.RandomFlameGeneratorSampler;
import org.jwildfire.create.tina.randomgradient.RandomGradientGenerator;
import org.jwildfire.create.tina.randomgradient.RandomGradientGeneratorList;
import org.jwildfire.create.tina.randomsymmetry.RandomSymmetryGenerator;
import org.jwildfire.create.tina.randomsymmetry.RandomSymmetryGeneratorList;
import org.jwildfire.create.tina.randomweightingfield.RandomWeightingFieldGenerator;
import org.jwildfire.create.tina.randomweightingfield.RandomWeightingFieldGeneratorList;
import org.jwildfire.create.tina.swing.*;
import org.jwildfire.create.tina.swing.MainEditorFrame;
import org.jwildfire.create.tina.base.*;
import java.io.*;







public class RandomBatchLoopThread extends Thread {


  //private final Prefs prefs;
  //private final ProgressUpdater mainProgressUpdater;
  //private final int maxCount;
  //private final List<SimpleImage> imgList;
  //private final List<FlameThumbnail> randomBatch;
  private final RandomFlameGenerator randGen;
  private final RandomSymmetryGenerator randSymmGen;
  private final RandomGradientGenerator randGradientGen;
  private final RandomWeightingFieldGenerator randomWeightingFieldGen;
  private final RandomBatchQuality pQuality;
  private final int pCount;
  private final TinaController tinaController;


  public RandomBatchLoopThread(TinaController tinaController, int pCount, RandomFlameGenerator randGen, RandomSymmetryGenerator randSymmGen, RandomGradientGenerator randGradientGen, 
        RandomWeightingFieldGenerator randomWeightingFieldGen, RandomBatchQuality pQuality) {
    this.pCount = pCount;
    this.randGen = randGen;
    this.randSymmGen = randSymmGen;
    this.randGradientGen = randGradientGen;
    this.randomWeightingFieldGen = randomWeightingFieldGen;
    this.pQuality = pQuality;
    this.tinaController = tinaController;
  }

  //public TinaController tinaController;

  public void run(){
    System.out.println("RandomBatchLoopThread running");
    while (true){
      System.out.println("RandomBatchLoopThread looping");
      tinaController.createRandomBatch(pCount, randGen, randSymmGen, randGradientGen, randomWeightingFieldGen, pQuality);
      System.out.println("RandomBatchLoopThread after tinavcont");

      // LOOP Params:
      int minLoopSleepTime = 20000;
      int renderQuality = 500; // 320 is high, 500 is very high
      int renderHeight = 1080; //
      int renderWidth = 1920; //


      try {
        TimeUnit.MILLISECONDS.sleep(minLoopSleepTime);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }






      //tinaController.renderImageButton_actionPerformed(true);

      final Flame flame = tinaController.getCurrFlame();

      String filepath = System.getProperty("user.dir");
      File file = new File(filepath + "/loopingflame.png");
      System.out.println(file);

      tinaController.prefs.setLastOutputImageFile(file);

      System.out.println("before starting the main thread");

      QualityProfile qualProfile = tinaController.getQualityProfile();
      ResolutionProfile resProfile = tinaController.getResolutionProfile();

      //System.out.println("Quality: " + qualProfile.getQuality());
      //System.out.println("Resolution: " + resProfile.getHeight());
      //System.out.println("Resolution: " + resProfile.getWidth());
      qualProfile.setQuality(renderQuality);
      resProfile.setHeight(renderHeight);
      resProfile.setWidth(renderWidth);

      RenderMainFlameThread mainRenderThreadLoop = new RenderMainFlameThread(tinaController.prefs, flame, file, tinaController.getQualityProfile(), 
          tinaController.getResolutionProfile(), tinaController.finishEventMember_, tinaController.mainProgressUpdater);

      Thread worker = new Thread(mainRenderThreadLoop);
      worker.setPriority(Thread.MIN_PRIORITY);
      worker.start();




      while (!mainRenderThreadLoop.isFinished()) {
        try {
          TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("Is finished?: " + mainRenderThreadLoop.isFinished());


      }

      System.out.println("Is finished OUTOUT?: " + mainRenderThreadLoop.isFinished());

      System.out.println("after starting the main thread");

      //enableMainRenderControls();
      //System.out.println("START");

      //Thread worker = new Thread(mainRenderThread);
      //worker.setPriority(Thread.MIN_PRIORITY);
      //worker.start();
      //System.out.println("STOP");
    }
  }

  // public CreateRandomBatchThread(TinaController parentController, ProgressUpdater mainProgressUpdater, int maxCount, List<SimpleImage> imgList, List<FlameThumbnail> randomBatch, RandomFlameGenerator randGen, RandomSymmetryGenerator randSymmGen, RandomGradientGenerator randGradientGen, RandomWeightingFieldGenerator randWeightingFieldGen, RandomBatchQuality quality) {
  //   this.parentController = parentController;
  //   this.mainProgressUpdater = mainProgressUpdater;
  //   this.maxCount = maxCount;
  //   this.imgList = imgList;
  //   this.randomBatch = randomBatch;
  //   this.randGen = randGen;
  //   this.randSymmGen = randSymmGen;
  //   this.randGradientGen = randGradientGen;
  //   this.randWeightingFieldGen = randWeightingFieldGen;
  //   this.quality = quality;
  //   this.prefs = Prefs.getPrefs();
  // }

  
}