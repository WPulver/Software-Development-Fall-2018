package edu.augustana.csc285.dowitcher;

import edu.augustana.csc285.dowitcher.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ScheduledExecutorService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import datamodel.AnimalTrack;
import datamodel.ProjectData;
import datamodel.TimePoint;

//import application.TimePoint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.util.Duration;

public class CalibrationWindowController {

	@FXML
	private AnchorPane appArea;

	@FXML
	private AnchorPane currentFrameWrapper;

	@FXML
	private ImageView currentFrameImage;
	@FXML
	private Slider sliderSeekBar;
	@FXML
	private TextArea currentFrameArea;
	@FXML
	private TextArea totalFrameArea;

	@FXML
	private TextField startFrame;
	@FXML
	private TextField endFrame;
	@FXML
	private TextField numChicks;
	@FXML
	private Button submitBtn;

	// a timer for acquiring the video stream
	// private ScheduledExecutorService timer;
	private VideoCapture capture = new VideoCapture();
	private String fileName = null;
	private int curFrameNum;
	private double numFrame;

	private int start;
	private int end;
	private int numChick = 5;
	private int pixelPerCm;


	@FXML
	public void initialize() {
		sliderSeekBar.setDisable(true);

	}

	@FXML
	private void handleSubmit() {
		start = Integer.parseInt(startFrame.getText());
		end = Integer.parseInt(endFrame.getText());
		numChick = Integer.parseInt(numChicks.getText());
		System.out.println(start + " " + end + " " + numChick);

	}

	public void start(String fName) {
		this.fileName = fName;
		startVideo();
		currentFrameArea.appendText("Current frame: 0\n");
		runSliderSeekBar();
	
	}

	protected void startVideo() {

		// start the video capture
		this.capture.open(fileName);
		numFrame = this.capture.get(Videoio.CV_CAP_PROP_FRAME_COUNT);
		totalFrameArea.appendText("Total frames: " + (int) numFrame + "\n");
		sliderSeekBar.setDisable(false);
		updateFrameView();
		sliderSeekBar.setMax((int) numFrame);
		

	}

	/**
	 * Get a frame from the opened video stream (if any)
	 *
	 * @return the {@link Mat} to show
	 */
	private Mat grabFrame() {
		// init everything
		Mat frame = new Mat();

		// check if the capture is open
		if (this.capture.isOpened()) {
			try {
				// read the current frame
				this.capture.read(frame);

				// if the frame is not empty, process it to black and white color

				// if (!frame.empty()) { Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
				// }

			} catch (Exception e) {
				// log the error
				System.err.println("Exception during the image elaboration: " + e);
			}
		}

		return frame;
	}

	private void runSliderSeekBar() {

		sliderSeekBar.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				currentFrameArea.appendText("Current frame: " + ((int) Math.round(newValue.doubleValue())) + "\n");
				curFrameNum = (int) Math.round(newValue.doubleValue());
				capture.set(Videoio.CAP_PROP_POS_FRAMES, curFrameNum - 1);
				updateFrameView();
			}

		});
	}



	private void updateFrameView() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// effectively grab and process a single frame
				Mat frame = grabFrame();
				// convert and show the frame
				Image imageToShow = Utils.mat2Image(frame);
				currentFrameImage.setImage(imageToShow);
			}
		});

	}
}