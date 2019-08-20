package khanhkt.fe.edu.vn.readmagazinerssapp.Services;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import khanhkt.fe.edu.vn.readmagazinerssapp.Helper.DatabaseUtilities;
import khanhkt.fe.edu.vn.readmagazinerssapp.Model.RssFeedModel;

public class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {
    private final String strRSS = "https://vnexpress.net/rss/tin-moi-nhat.rss";//address to access rss
    private List<RssFeedModel> mListResult;//get result parsing

    //helper variables
    private DatabaseUtilities dbUtils;//access DB

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            URL url = new URL(strRSS);
            InputStream inputStream = url.openConnection().getInputStream();//retrieve resource from internet
            //get feeds
            mListResult = parseFeed(inputStream);

            return true;
        } catch (IOException ex) {
            Log.e(ContentValues.TAG, "ERROR", ex);
        } catch (XmlPullParserException ex) {
            Log.e(ContentValues.TAG, "ERROR", ex);
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            Log.d(ContentValues.TAG, "SUCCESS");

        } else {
            Log.d(ContentValues.TAG, "FAILED");
        }
    }

    public List<RssFeedModel> getmListResult() {
        return mListResult;
    }

    //use Full Parser Exception to parse RSS to store Object Model
    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException,
            IOException {
        //Declare all information for preparing store in Model
        String title = null;
        String link = null;
        String description = null;
        String pubDate = null;
        //determine item tag to get data
        boolean isItem = false;
        //define list result
        List<RssFeedModel> result = new ArrayList<>();
        try {
            //using XMLPullParser to parse
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            //process parsing
            parser.nextTag();
            while (parser.next() != XmlPullParser.END_DOCUMENT) {//traversal to end document
                int currentCursor = parser.getEventType();//get current cursor point what kinds
                String tagName = parser.getName();//get current Tag that cursor point

                if (tagName == null) {//not tag
                    continue;
                }//end if tagName is null
                //process if start tag and tag name is item
                if (currentCursor == XmlPullParser.END_TAG) {
                    if (tagName.equalsIgnoreCase("item")) {
                        isItem = false;
                    }//end if closed tag is item
                    continue;
                }//end if cursor point End Element

                if (currentCursor == XmlPullParser.START_TAG) {
                    if (tagName.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }//end if started tag is item
                }//end if cursor point Start Element

                Log.d("MyXmlParser", "Parsing name ==> " + tagName);
                //get value of attribute in item
                String textContent = null;
                if (parser.next() == XmlPullParser.TEXT) {
                    textContent = parser.getText();
                    parser.nextTag();
                }//end if get text content

                //determine tagName to set attribute value in corresponding
                if (tagName.equalsIgnoreCase("title")) {
                    title = textContent;
                } else if (tagName.equalsIgnoreCase("link")) {
                    link = textContent;
                } else if (tagName.equalsIgnoreCase("description")) {
                    description = textContent;
                } else if (tagName.equalsIgnoreCase("pubDate")) {
                    pubDate = textContent;
                }//end if processing is assigned value to specific attribute

                //Store value in to model
                if (title != null
                        && link != null
                        && description != null
                        && pubDate != null) {
                    if(isItem) {
                        RssFeedModel item = new RssFeedModel(title, link, description, pubDate);
                        result.add(item);

                        Log.d("MyXmlParser", "Parsing value ==> "
                                + title + " - " + link + " - " + description + " - " + pubDate);
                    }  //end if item tag is processed
                    //reset all value to next item
                    title = null;
                    link = null;
                    description = null;
                    pubDate = null;
                    isItem = false;
                }//end if storing to model
            }//end while for traversal
            return result;
        } finally {
            //do something
        }
    }
}
