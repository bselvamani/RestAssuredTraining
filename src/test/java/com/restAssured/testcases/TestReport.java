package com.restAssured.testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.restAssured.helper.Project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestReport{

    private static ExtentReports extent;

    public static ExtentReports generateReport() {
        Path reportDir = Path.of(Project.ROOT, "reports");
        try {
            Files.createDirectories(reportDir);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to create reports directory", ex);
        }

        String html_path = reportDir.resolve("TestReports.html").toString();
        ExtentSparkReporter reporter = new ExtentSparkReporter(html_path);
        reporter.config().setReportName("RestAssured Reports");
        reporter.config().setDocumentTitle("RestAPI Automation Reports");

        extent = new ExtentReports();
        extent.attachReporter(reporter);

        return extent;
    }
}