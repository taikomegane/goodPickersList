package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@SuppressWarnings("deprecation")

public class pickersList{
	public ArrayList<Info> execute(String url){
		String[] pickers;
		ArrayList<String> goodPickers;
		ArrayList<String> goodPickersName;
		ArrayList<String[]> goodPickersFollowInfo;
		ArrayList<Info> pickersInfo = new ArrayList<Info>();

		try {
			//PickerのIDリストを生成
			pickers = getPickers(url);

			//Pickerの内1000人以上followers, followingいる人のIDをリストにする
			goodPickers = getGoodPickers(pickers);

			//goodPickersの名前をリストにする
			goodPickersName = getGoodPickersName(goodPickers);

			//follow, followingの数をリストにする
			goodPickersFollowInfo = getGoodPickersFollowInfo(goodPickers);

			for (int i = 0; i < goodPickers.size(); i++){
				//リストから各情報を用いてInfoインスタンスを生成する
				Info info = new Info (goodPickers.get(i), goodPickersName.get(i), goodPickersFollowInfo.get(i)[0], goodPickersFollowInfo.get(i)[1]);

				//infoインスタンスのリストを生成する
				pickersInfo.add(info);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return pickersInfo;
	}

	@SuppressWarnings("resource")
 	public static String[] getPickers(String url) throws IOException {

		// StringBuilderを使って可変長の文字列を扱う
		StringBuilder builder = new StringBuilder();

		// HttpClientのインスタンスを作る
		HttpClient client = new DefaultHttpClient();

		// HttpGetのインスタンスを作る
		HttpGet httpGet = new HttpGet(url);

		String[] pickers = null;

		try {
			// リクエストしたリンクが存在するか確認するために、HTTPリクエストを送ってHTTPレスポンスを取得する
			HttpResponse response = client.execute(httpGet);

			// 返却されたHTTPレスポンスの中のステータスコードを調べる
			int statusCode = response.getStatusLine().getStatusCode();

			//HTTPレスポンスが200ならページがある
			if (statusCode == 200) {
				// レスポンスからHTTPエンティティ（実体）を生成
				HttpEntity entity = response.getEntity();

				// HTTPエンティティからコンテント（中身）を生成
				InputStream content = entity.getContent();

				// コンテントからInputStreamReaderを生成し、さらにBufferedReaderを作る
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;

				// readerからreadline()で行を読んで、builder文字列(StringBuilderクラス)に格納していく。
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}

				//取得したbuilder文字列からpickersのユーザIDの配列を取得する
				int start = builder.indexOf("[", builder.indexOf("pickers")) + 1;
				int end = builder.indexOf("]", start);
				String str = builder.substring(start, end);
				pickers = str.split(",", 0);

			} else {
				System.out.println("Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pickers;
	}

	@SuppressWarnings("resource")
	public static ArrayList<String> getGoodPickers(String[] pickers) throws IOException{
		//1000人以上フォローしている（されている）人がいるユーザ用ArrayList
		ArrayList<String> goodPickers = new ArrayList<String>();

		for (int i = 0; i < pickers.length; i++) {
			//urlを生成
			String url = "https://newspicks.com/user/" + pickers[i];

			// HttpClientのインスタンスを作る
			HttpClient client = new DefaultHttpClient();

			// HttpGetのインスタンスを作る
			HttpGet httpGet = new HttpGet(url);

			// リクエストしたリンクが存在するか確認するために、HTTPリクエストを送ってHTTPレスポンスを取得する
			HttpResponse response = client.execute(httpGet);

			// 返却されたHTTPレスポンスの中のステータスコードを調べる
			int statusCode = response.getStatusLine().getStatusCode();

			//HTTPレスポンスが200ならページがある
			if (statusCode == 200) {

			//スクレイピング
			Document doc = Jsoup.connect(url).get();

			//pickersのfollower, followingを取り出す
			ArrayList<Element> userCount = doc.getElementsByClass("count");

			//follower, followingの数を整数に変換
			int followers = Integer.parseInt(userCount.get(0).text());
			int following = Integer.parseInt(userCount.get(1).text());

			//1000人以上ならgoodUerListに代入
			if (followers >= 1000 && following >= 1000){
				goodPickers.add(pickers[i]);
			}
			}
		}
		return goodPickers;
	}

	public static ArrayList<String> getGoodPickersName (ArrayList<String> goodPickers) throws IOException{
		ArrayList<String> goodPickersName = new ArrayList<String>();
		for (int i = 0; i < goodPickers.size(); i++) {
			Document doc = Jsoup.connect("https://newspicks.com/user/" + goodPickers.get(i)).get();
			ArrayList<Element> name = doc.getElementsByClass("username");
			goodPickersName.add(name.get(0).text());
		}
		return goodPickersName;
	}

	public static ArrayList<String[]> getGoodPickersFollowInfo (ArrayList<String> goodPickers) throws IOException{
		ArrayList<String[]> goodPickersFollowInfo = new ArrayList<String[]>();
		for (int i = 0; i < goodPickers.size(); i++) {
			//スクレイピング
			Document doc = Jsoup.connect("https://newspicks.com/user/" + goodPickers.get(i)).get();

			//pickersのfollower, followingを取り出す
			ArrayList<Element> count = doc.getElementsByClass("count");

			//フォロー情報の配列を用意
			String[] followInfo = {count.get(0).text(), count.get(1).text()};

			//フォロー情報をgoodPickersFollowInfoに代入
			goodPickersFollowInfo.add(followInfo);
		}

		return goodPickersFollowInfo;
	}
}