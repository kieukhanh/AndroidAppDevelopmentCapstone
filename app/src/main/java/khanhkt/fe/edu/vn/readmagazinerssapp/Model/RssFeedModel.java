package khanhkt.fe.edu.vn.readmagazinerssapp.Model;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

//describe information to contain data retriew from RSS
public class RssFeedModel {
    private String title; //get Title from RSS
    private String link; //get Link from RSS
    private String description; //get description from RSS
    private String publicDate; //get public Date from RSS

    public RssFeedModel() {
    }

    public RssFeedModel(String title, String link, String description, String publicDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.publicDate = publicDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate;
    }
}

