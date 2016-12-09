package com.kwokstudio.fuselage.bean;

import java.util.List;

/**
 * Created by 郭垒 on 2016/11/15.
 */

public class TextContent {

    /**
     * body :
     * title : 更多日常心理学，都在读读日报里
     * share_url : http://daily.zhihu.com/story/7483384
     * js : []
     * theme : {"thumbnail":"http://pic3.zhimg.com/0e71e90fd6be47630399d63c58beebfc.jpg","id":13,"name":"日常心理学"}
     * ga_prefix : 111823
     * type : 0
     * id : 7483384
     * css : ["http://news-at.zhihu.com/css/news_qa.auto.css?v=4b3e3"]
     */

    private String body;
    private String title;
    private String share_url;
    private ThemeBean theme;
    private String ga_prefix;
    private int type;
    private int id;
    private List<?> js;
    private List<String> css;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public ThemeBean getTheme() {
        return theme;
    }

    public void setTheme(ThemeBean theme) {
        this.theme = theme;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<?> getJs() {
        return js;
    }

    public void setJs(List<?> js) {
        this.js = js;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    public static class ThemeBean {
        /**
         * thumbnail : http://pic3.zhimg.com/0e71e90fd6be47630399d63c58beebfc.jpg
         * id : 13
         * name : 日常心理学
         */

        private String thumbnail;
        private int id;
        private String name;

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
