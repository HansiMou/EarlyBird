<html>
  <head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/png" href="assets/i/favicon.png">
    <link rel="stylesheet" href="assets/css/amazeui.min.css"/>
    <link rel="stylesheet" href="assets/css/app.css">
    <link rel="stylesheet" href="assets/css/my.css">
  </head>
  <body>
  <?php $query = $_POST['query'];
  if (empty($query)){
  echo '
    <table width="500" height="0%" border="0" align="center"  cellpadding="0" cellspacing="0">
    <form name="form1" action="index.php" method="POST" >
      <tr><td height="25%"></td></tr>
      <tr><td height="10%"><h2>Flash ChemLit</h2></td></tr>
      <tr>
        <td height="30" align="center"><p><input type="text" name="query" class="am-form-field am-radius"/></p></td>
      </tr>
      <tr><td height="2%"></td></tr>
      <tr><td height="25" align="center"><input class="submit_btn" id="submit_btn" type="submit" name="submit" value="Search"></td></tr>
      <tr></tr>
      <tr></tr>

    <tr><td height="25" align="center"><p>
      Last Updated:'; $a=filemtime("cache.txt");
            echo date("Y-m-d H:i:s",$a); echo '
        </p>
    </td></tr>
    <tr><td height="25" align="center"><p>
    <small>&#169; 2016 HANSI MOU ALL RIGHTS RESERVED</small></p>
    </td></tr>
    </table>
    </form>
  ';
  }
  else {
      echo '
<header class="am-topbar am-topbar-fixed-top">
<div class="am-container">
  <h1 class="am-topbar-brand">
    <a href="" font-family="Optima">Flash ChemLit</a>
  </h1>
  
  <form action="index.php" method="POST" class="am-topbar-form am-topbar-left am-form-inline" role="search">
    <div class="am-form-group">
      <input type="text" name="query" class="am-form-field am-input-sm topsearch" value="';echo $query;echo '">
      <button type="submit" class="am-btn am-btn-primary am-btn-sm">SEARCH</button>
    </div>
  </form>
  
</div>
</header>';
echo '



<div class="am-g am-g-fixed blog-g-fixed get">
<div class="am-u-md-10">';

$w = system("cd /Users/hans/Documents/workspace/Flash\ ChemLit; java -cp .:lib/lucene-core-5.4.1.jar:lib/lucene-analyzers-common-5.4.1.jar:lib/lucene-queryparser-5.4.1.jar:lib/lucene-highlighter-5.4.1.jar:lib/lucene-join-5.4.1.jar:lib/lucene-memory-5.4.1.jar:bin/ Searcher \"".$_POST['query'] . "\"",$res);
echo '</div>

</div>
<footer class="blog-footer">
<p>Last Updated: ';$a=filemtime("cache.txt");
      echo date("Y-m-d H:i:s",$a); echo '<br/>
  <small>&#169; 2016 HANSI MOU ALL RIGHTS RESERVED</small>
</p>
</footer>
';

  }
  ?>
      
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/amazeui.min.js"></script>
  </body>
</html>