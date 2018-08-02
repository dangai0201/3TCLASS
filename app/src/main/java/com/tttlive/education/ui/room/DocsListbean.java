package com.tttlive.education.ui.room;

import java.util.List;

/**
 * Created by Administrator on 2018/3/12/0012.
 */

public class DocsListbean {


    /**
     * list : [{"id":"267","type":"2","htmlUrl":"https://3tdoc.oss-cn-beijing.aliyuncs.com/h5/2d740137500f2d8eac0f3ea742498af3/2018/05/04/13/2727/index/index.html","imgSrc":"http://3tdoc.oss-cn-beijing.aliyuncs.com/docs/2018/05/04/13/2705_7123-0.jpg","imgSrcData":[],"originalName":"如何制作PPT文件H5.ppt","originalSrc":"http://dev.xinshenglan.3ttech.cn/upload/2018/05/04/13/2705/yRpcuRziXeEoalv1525411625.ppt","page":"20","created":"1525411625","fileSize":"1944064"}]
     * count : 4
     * page_count : 1
     */

    private int count;
    private int page_count;
    private List<ListBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 267
         * type : 2
         * htmlUrl : https://3tdoc.oss-cn-beijing.aliyuncs.com/h5/2d740137500f2d8eac0f3ea742498af3/2018/05/04/13/2727/index/index.html
         * imgSrc : http://3tdoc.oss-cn-beijing.aliyuncs.com/docs/2018/05/04/13/2705_7123-0.jpg
         * imgSrcData : []
         * originalName : 如何制作PPT文件H5.ppt
         * originalSrc : http://dev.xinshenglan.3ttech.cn/upload/2018/05/04/13/2705/yRpcuRziXeEoalv1525411625.ppt
         * page : 20
         * created : 1525411625
         * fileSize : 1944064
         */

        private String id;
        private String type;
        private String htmlUrl;
        private String imgSrc;
        private String originalName;
        private String originalSrc;
        private String page;
        private String created;
        private String fileSize;
        private List<?> imgSrcData;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getHtmlUrl() {
            return htmlUrl;
        }

        public void setHtmlUrl(String htmlUrl) {
            this.htmlUrl = htmlUrl;
        }

        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }

        public String getOriginalName() {
            return originalName;
        }

        public void setOriginalName(String originalName) {
            this.originalName = originalName;
        }

        public String getOriginalSrc() {
            return originalSrc;
        }

        public void setOriginalSrc(String originalSrc) {
            this.originalSrc = originalSrc;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getFileSize() {
            return fileSize;
        }

        public void setFileSize(String fileSize) {
            this.fileSize = fileSize;
        }

        public List<?> getImgSrcData() {
            return imgSrcData;
        }

        public void setImgSrcData(List<?> imgSrcData) {
            this.imgSrcData = imgSrcData;
        }
    }
}
