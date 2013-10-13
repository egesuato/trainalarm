package it.egesuato.trainalarm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;

import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

public class TrainChecker {

	public static final String TAG = TrainChecker.class.getSimpleName();
	private static final String NO_INFO_AVAILABLE_OR_WRONG_TRAIN = "No info available or wrong train number";
	private static final String ERROR_DURING_RETRIEVAL = "ERROR DURING RETRIEVAL";
	private String url = "http://mobile.viaggiatreno.it/vt_pax_internet/mobile";

	private HttpPost httpPost;
	private HttpClient httpClient;

	public TrainChecker() {
		httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 2000);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 2000);
		httpPost = new HttpPost(url);

	}

	/**
	 * Connects to viaggiatreno.it, submit the form about train number and parse
	 * the resulting page. From resulting page it tries to obtain the status of
	 * selected train. The result will be sent as a message in the handler
	 * passed in the constructor.
	 * 
	 * @param trainNumber
	 *            to be checked
	 */
	public String checkTrainStatus(int trainNumber) {
		String checkResult = NO_INFO_AVAILABLE_OR_WRONG_TRAIN;
		Log.i(TAG, "Checking url " + url + " for train " + trainNumber);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("numeroTreno", String.valueOf(trainNumber)));

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(httpPost);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()), 8096);
			String currentLine = br.readLine();
			StringBuffer buffer = new StringBuffer();

			while (currentLine != null) {
				buffer.append(currentLine);
				currentLine = br.readLine();
			}

			String string = buffer.toString();

			String beginning = "<div  class=\"evidenziato\"><strong>";

			if (string.indexOf(beginning) != -1) {
				string = string.substring(string.indexOf(beginning)
						+ beginning.length() + 1);

				if (string.indexOf("<br>") != -1) {
					string = string.substring(0, string.indexOf("<br>"));

					string = string.replace("\t", " ");

					checkResult = string.trim();
				}
			}

		} catch (Exception e) {
			Log.e(TAG,
					"Errors connecting url " + url + " error: "
							+ e.getMessage());
			checkResult = ERROR_DURING_RETRIEVAL;
		}
		// the resulting html needs to be html-parsed to be fully readable
		Spanned fromHtml = Html.fromHtml(checkResult);

		return "Train " + trainNumber + ": " + fromHtml.toString();
	}

}