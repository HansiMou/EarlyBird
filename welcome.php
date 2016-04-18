
<?php
$w = system("/usr/bin/java -cp '/home/hm1305/public_html/Lucene/*:/home/hm1305/public_html/HTMLParser/*:/home/hm1305/public_html/src' Searcher \"".$_POST['query'] . "\"",$res);

?>
<html>
<body>