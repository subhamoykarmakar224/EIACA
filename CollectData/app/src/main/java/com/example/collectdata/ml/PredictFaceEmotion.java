package com.example.collectdata.ml;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.example.collectdata.bean.Recognition;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.image.ops.Rot90Op;
import org.tensorflow.lite.support.image.ops.TransformToGrayscaleOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PredictFaceEmotion {

    private static final float PROBILITY_MEAN = 0.0f;
    private static final float PROBABILITY_STD = 255.0f;
    private static final float IMAGE_STD = 1.0f;
    private static final float IMAGE_MEAN = 0.0f;
    private static final int MAX_SIZE = 5; // Top 5 results

//    private static final String MODEL_URI = "model_mobile_net.tflite";
    private static final String MODEL_URI = "EmotionRecognition.tflite";
    private final int imageResizeX;
    private final int imageResizeY;
    private TensorImage inputImageBuffer;
    private final TensorBuffer probabilityImageBuffer;
    private final TensorProcessor probabilityProcessor;
    private final Interpreter tensorClassifier;
    List<String> labels;

    public PredictFaceEmotion(Activity activity) throws IOException {
        MappedByteBuffer classifierModel = FileUtil.loadMappedFile(
                activity, MODEL_URI
        );

//        emotion_label_to_text = {0:'anger', 1:'disgust', 2:'fear',
//        3:'happiness', 4: 'sadness', 5: 'surprise', 6: 'neutral'}
        labels = new ArrayList<>();
        labels.add("0");
//        labels.add("1");
//        labels.add("2"); //
//        labels.add("3");
//        labels.add("4");
//        labels.add("5");
//        labels.add("6");


        tensorClassifier = new Interpreter(classifierModel, null);
        Log.i("ollo", "Output Tensor Count: " + tensorClassifier.getOutputTensorCount());

        int imageTensorIndex = 0; // input
        int probabilityTensorIndex = 0; // output

        // Creating Buffer for recognition for Input image
        int[] inputImageShape = tensorClassifier.getInputTensor(imageTensorIndex).shape();
        DataType inputDataType = tensorClassifier.getInputTensor(imageTensorIndex).dataType();


        // Creating Buffer for recognition for Output image
        int[] outputImageShape = tensorClassifier.getInputTensor(imageTensorIndex).shape();
        DataType outputDataType = tensorClassifier.getInputTensor(imageTensorIndex).dataType();


        imageResizeX = inputImageShape[1];
        imageResizeY = inputImageShape[2];

        inputImageBuffer = new TensorImage(inputDataType);
        probabilityImageBuffer = TensorBuffer.createFixedSize(
                outputImageShape, outputDataType
        );

        probabilityProcessor = new TensorProcessor.Builder()
                .add(new NormalizeOp(PROBILITY_MEAN, PROBABILITY_STD))
                .build();
    }

    /**
     * Actual Image Recognition
     *
     * @param bitmap
     * @param sensorOrientation
     * @return List of probabilities
     */
    public List<Recognition> recognizeImage(final Bitmap bitmap, final int sensorOrientation) {
        List<Recognition> recognitions = new ArrayList<>();



        inputImageBuffer = loadImage(bitmap, sensorOrientation);
//        byte[] pixels = ((DataBufferByte) inputImageBuffer.getRaster().getDataBuffer()).getData();
//        int cnt = 0;
//        String s = "";
//        while (cnt <= 30) {
//            s = s + " (" + Color.red(bitmap.getPixel(cnt, cnt)) + ", " +Color.green(bitmap.getPixel(cnt, cnt)) + ", " +Color.blue(bitmap.getPixel(cnt, cnt)) + ", " + ")";
//            cnt ++;
//        }
//        Log.i("ollo", "Pixels: " + s);

        Log.i("ollo", "Image Size: " + inputImageBuffer.getBuffer().toString());
        Log.i("ollo", "Image Size: " + probabilityImageBuffer.getBuffer().toString());
        Log.i("ollo", "Image Size: " + probabilityImageBuffer.getBuffer().rewind());
        tensorClassifier.run(inputImageBuffer.getBuffer(), probabilityImageBuffer.getBuffer().rewind());
        Map<String, Float> labelledProbability = new TensorLabel(
                labels, probabilityProcessor.process(probabilityImageBuffer)
        )
                .getMapWithFloatValue();
        for(Map.Entry<String, Float> entry : labelledProbability.entrySet()) {
            recognitions.add(new Recognition(entry.getKey(), entry.getValue()));
        }

        Collections.sort(recognitions);
        recognitions.subList(0, MAX_SIZE > recognitions.size() ? recognitions.size() : MAX_SIZE).clear();
        return recognitions;
    }

    private TensorImage loadImage(Bitmap bitmap, int sensorOrientation) {
        inputImageBuffer.load(bitmap);
        int noOfRotations = sensorOrientation / 90;
//        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        int cropSize = Math.min(96, 96);
        ImageProcessor imageProcessor = new ImageProcessor.Builder()
                .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                .add(new ResizeOp(imageResizeX, imageResizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                .add(new Rot90Op(noOfRotations))
                .add(new TransformToGrayscaleOp())
                .add(new NormalizeOp(IMAGE_MEAN, IMAGE_STD))
                .build();
        return imageProcessor.process(inputImageBuffer);
    }
}


























