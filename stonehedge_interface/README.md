<h1>Coming across the following errors?</h1>

<h2>Error 1</h2> 
      
      I/launcher - Running 1 instances of WebDriver
      I/direct - Using ChromeDriver directly...
      E/direct - Error code: 135
      E/direct - Error message: Could not find update-config.json. Run    'webdriver-manager update' to download binaries.
      E/direct - Error: Could not find update-config.json. Run 'webdriver-manager update' to download binaries.

<h4>Solution</h4>
Go to package.json and run the "webdriver-update" under "scripts". 
You can run it through the command line with "npm run webdriver-update" or 
just click on it directly in IntelliJ. IntelliJ might warn you that it is not 
set up correctly -- that is okay -- press "Continue Anyways".

<h2>Error 2</h2> 
* What went wrong:

      Execution failed for task ':foo:nodeSetup'. 
      Could not resolve all files for configuration ':foo:detachedConfiguration1'.
      Could not find org.nodejs:node:7.5.0.
      Searched in the following locations:
        - https://nodejs.org/dist/v7.5.0/ivy.xml
      Required by:
          project :foo
          
<h4>Solution</h4>
https://stackoverflow.com/questions/59545881/upgrading-gradle-to-6-0-requires-ivy-xml-and-pom-files/59554622#59554622
   * To be more specific, the answer which tells you to need to use the forked version of the plugin
   * It seems that the old plugin does not work with Gradle 5+, and if you look at the nodejs "dist/" folder,
   there are no .ivy files to be found.
