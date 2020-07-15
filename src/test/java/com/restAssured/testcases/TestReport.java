package com.restAssured.testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.restAssured.helper.Project;

public class TestReport{

    private static ExtentReports extent;

    public static ExtentReports generateReport() {
        String html_path = Project.ROOT + "/reports/TestReports.html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(html_path);
        reporter.config().setReportName("RestAssured Reports");
        reporter.config().setDocumentTitle("RestAPI Automation Reports");

        extent = new ExtentReports();
        extent.attachReporter(reporter);

        return extent;
    }
}