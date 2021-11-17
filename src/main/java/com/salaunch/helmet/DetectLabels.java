package com.salaunch.helmet;

import java.util.List;

import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;
import software.amazon.awssdk.services.rekognition.model.S3Object;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
// snippet-end:[rekognition.java2.detect_labels.import]

/**
 * To run this Java V2 code example, ensure that you have setup your development
 * environment, including your credentials.
 *
 * For information, see this documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 */
public class DetectLabels {

	private String ACCESS_KEY = "";
	private String SECRET_KEY = "";

	public static void main(String[] args) {

		final String USAGE = "\n" + "Usage: " + "   <sourceImage>\n\n" + "Where:\n"
				+ "   sourceImage - the path to the image (for example, C:\\AWS\\pic1.png). \n\n";

		String sourceImage;
		if (args.length == 1) {
			sourceImage = args[0];
		} else {
			sourceImage = "";
		}

		Region region = Region.EU_CENTRAL_1;
		RekognitionClient rekClient = RekognitionClient.builder().region(region).build();

		detectImageLabels(rekClient, sourceImage);
		rekClient.close();
	}

	// snippet-start:[rekognition.java2.detect_labels.main]
	public static void detectImageLabels(RekognitionClient rekClient, String sourceImage) {

		try {

			InputStream sourceStream = new URL(
					"https://images.unsplash.com/photo-1557456170-0cf4f4d0d362?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8bGFrZXxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&w=1000&q=80")
							.openStream();
			// InputStream sourceStream = new FileInputStream(sourceImage);
			SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);

			// Create an Image object for the source image.
			Image souImage = Image.builder().bytes(sourceBytes).build();
	        DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder().
	        		image(Image.builder().s3Object(
	        				S3Object.builder().name("helmet1.jpg").bucket("helmetbucket").build())
	        		.build()).
	        		build();

			DetectLabelsRequest detectLabelsRequest2 = DetectLabelsRequest.builder().image(souImage).maxLabels(10)
					.build();

			DetectLabelsResponse labelsResponse = rekClient.detectLabels(detectLabelsRequest);
			List<Label> labels = labelsResponse.labels();

			System.out.println("Detected labels for the given photo");
			for (Label label : labels) {
				System.out.println(label.name() + ": " + label.confidence().toString());
			}

		} catch (RekognitionException | FileNotFoundException | MalformedURLException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// snippet-end:[rekognition.java2.detect_labels.main]
}