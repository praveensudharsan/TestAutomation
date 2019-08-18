# TestAutomation Framework

This is the sample Selenium Java TestNg Maven framework which consist of the sample test for Amazon and page objects for the tests.<br>
<b>Key points:</b><br>
<ol>
  <li>Selected TestNg framework for parallel execution and test handling. [Groups, Annotations, Parallel Execution]</li>
  <li>Integrated with Extent report for reporting. In addition to default testng report, project is integrated with extent report for better reporting.</li>
  <li>Project can be integrated with any CI tool using the surfire plugin configured in POM</li>
  <li>Project can be further inetragted with any tools like OWASP, RestAssured.</li>
</ol>

# Repository Structure Details
<ol>
<li><b>src/test/java :</b> This folder have the page objects and tests</li>
<li><b>pom.xml :</b> This xml file consists of all the required dependecies for the project</li>
<li><b>TestNgXML :</b> This folder will contain the testng xml file to trigger the execution</li>
<li><b>Common/Drivers :</b> This folder have the drivers to launch the browsers</li>
<li><b>test-output :</b> This folder consists of the report of this suite.</li>
</ol>


# Page Object Details
<ol>
  <li><b>src/test/java/Amazon : </b> This folder consists of the application specific reusable methods and tests.</li>
  <li><b>src/test/java/genericLibrary : </b>This folder consists of the non-application specific libraries. For e.g.: Browser launch, Reporting</li>
</ol>

# Automation Report
<ul><li><b>test-output/AutomationExtentReport.html :</b> Execution report of the assignment. Same test is executed against the chrome and firefox in parallel through testng xml.</li></ul>

# How to trigger the execution
<ul>
  <li>Import this project into eclipse</li>
  <li>Updated the maven project</li>
  <li>Right click on the testngxml and click run as testng suite</li>
  <li>Note: If Chrome/Firefox version is different, please place the corresponding driver in Common/Drivers</li>
  <li>Check the results in test-output</li>
</ul>
