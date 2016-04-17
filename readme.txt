Tools:
Crawler: Jsoup, HtmlUnit, Selenium
Searcher: Lucene

Difficulties

1. get the page
When trying to get the page, some part of web content is not included. It turned out to be a dynamic webpage. 

I firstly used WebDriver to implement this. It turned out to be too slow. At last I use the Jsoup to do this.

But later there are websites that I have to use webdriver/selenium to do this job. At the end I used a hybrid method.

When deploying the project to the cims, the chromedriver and firefox driver cannot run normally due to lack of the configuration. To make the selenium run, I have to install new software or so as a sudo user.

Then I tried to use another tools - HtmlUnitDriver to do the job, which turns out to be much faster because there is no graphic interface.

2. tried to generic
Initially I tried to write a class that can take in the configuration file and do the job, but later I found it is impossible due to difference between pages and links. At the end I used an base class for all the crawler and implement specific class for each website which extends the base class.