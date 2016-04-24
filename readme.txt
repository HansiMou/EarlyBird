1. URL

cs.nyu.edu/~hm1305/fclp/index.php


2. How-to

To compile:
javac -d class -cp .:./lib/* ./src/*.java

To run the crawler and build index:
java -cp ./class/:./lib/*:. FlashChemLit

To host the page:
There is an index.php file, and that is the main and the only page.


3. write-up

Objective of the project.

This project is focused on solving real-world problem. The problem is that, current scholar search systems like Google Scholar update every two weeks.  which is not frequent enough for phd students, who should prepare themselves to read the newest paper all the time. In that case, if he wants to know the newest literature, he has to go through all the magazine website and get used to the different layout and search position.

My goal is to build a search engine that updates several times per day, and keep a set of literature within most recent few week (configurable). Since it is specially designed for my friend, I narrow the field down to the chemistry. Of course it can be applicable to all literature, which is just a matter of time.



Sketch of the architecture

1. I installed an scheduled task that will run my crawler several times a day. 
2. In the crawler, the index is incremental updated. I used a cache file to preserver all the pages that I downloaded. The outdated record will also be 
deleted. 
3. Search engine will use the output of the java program to display the search results. The java program uses the search functionality of Lucene.


List of external software to be used
Jsoup, HtmlUnit, Selenium, Lucene


List of web resources to be used.
Amaze UI, ACS, Science, Nature, Wiley


Other pertinent system features
Languages: Java, php, html, javascript


Examples:

Since my search engine features freshness, my example here probably will not appear when you evaluate my system.

Except for some special cases, every literature is indexed.

For the links below, you can go any one of them and then find a literature that is within two weeks, then try search a key word in the search engine will give the result. Note: if the article is published to date, maybe it has not been indexed yet. 

http://science.sciencemag.org/ 
http://www.nature.com/nchembio/research/index.html 
http://www.nature.com/nmat/research/index.html 
http://www.nature.com/nchem/research/index.html 
http://www.nature.com/nature/research/chemical-sciences.html
http://onlinelibrary.wiley.com/journal/10.1002/(ISSN)1521-3773/earlyview 
http://onlinelibrary.wiley.com/journal/10.1002/(ISSN)1521-3765/earlyview 
http://onlinelibrary.wiley.com/journal/10.1002/(ISSN)1861-471X/earlyview 

The failure happens when the link to certain article cannot be find in the page by any way. For example, 'editor's choice' and 'news' in 'Science' cannot be founded. They are nevertheless important. Those failure does not make an impact.

Difficulties

1. get the page
When trying to get the page, some parts of web content are not included. It turned out to be a dynamic webpage. 

Then I used WebDriver to implement this. It turned out to be too slow due to graphic interface. Then I found out Jsoup.

But later there are websites that I have to use webdriver/selenium, which can get all the links I want. I decided to use a hybrid method.

When deploying the project to the cims, the chromedriver and firefox driver cannot run normally due to lack of the software. Limited to my permission, I cannot do any changes to the CIMS server. But oo make the selenium run, I have to install new software or so as a sudo user.

Then I tried to use another tools - HtmlUnitDriver to do the job, which turns out to be much faster because there is no graphic interface.

2. tried to generic
Initially I tried to write a class that can take in the configuration file and can crawler all the different websites, but later I found it is impossible due to difference between pages and links. At the end I used an base class for all the crawler and implement specific class for each website which extends the base class.

I do maintain a config.txt, in which you can modify some configuration.

3. Robots.txt
The most important magazine for chemistry is ACS, which forbidden any kind of crawler except for Google, which is kind of mean. Considering its importance, I have been contacting the ACS to see what I can do. No feedback till I wrote this.


4. Scoring
Score system based on the field, document and journal title. Taken into consideration magazine impact factors.


5. Charset problem
Really annoying. At last with the help from CIMS support, problem is solved. The key to it to java -Dfile.encoding=UTF-8 ...

6. User Interface
Since my system is not just a course project, it is put into use already for weeks. It must have a friendly interface to satisfy students' needs. Images are their main focuses, and it takes a long time to implement the image gallery. Different toggle, label, font and color to highlight the text.

7. Easiness of maintainence 
Created a cache file, which save the time of downloading all the indexed and outdated pages. And index file will not grow too big since outdate records are deleted. 


