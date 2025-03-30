package com.github.hcsp.http;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Crawler {
    static class GitHubPullRequest {
        // Pull request的编号
        int number;
        // Pull request的标题
        String title;
        // Pull request的作者的 GitHub 用户名
        String author;

        GitHubPullRequest(int number, String title, String author) {
            this.number = number;
            this.title = title;
            this.author = author;
        }
    }

    // 给定一个仓库名，例如"golang/go"，或者"gradle/gradle"，返回第一页的Pull request信息
    public static List<GitHubPullRequest> getFirstPageOfPullRequests(String repo) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://github.com/hcsp/read-github-pull-requests/pulls"))
                .header("Content-Type", "application/json")
                .GET() // 或者 .POST(HttpRequest.BodyPublishers.ofString("请求体"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status code: " + response.statusCode());
//        System.out.println("Response body: " + response.body());
        String html = response.body();
        Document doc = Jsoup.parse(html);
        Element titleEle = doc.selectFirst("#issue_240_link");
        String title = titleEle != null ?String.valueOf(titleEle.childNodes().get(0)):"";
        System.out.println(title);
        List<GitHubPullRequest> result = Arrays.asList(new GitHubPullRequest(240,title,"rwwe"));
        return result;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        getFirstPageOfPullRequests("4");
    }
}
