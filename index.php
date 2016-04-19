<html>
  <head>
    <meta charset="UTF-8">
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
      <input type="text" name="query" class="am-form-field am-input-sm" value="';echo $query;echo '" size="40">
      <button type="submit" class="am-btn am-btn-primary am-btn-sm">SEARCH</button>
    </div>
  </form>
  
</div>
</header>';
echo '



<div class="am-g am-g-fixed blog-g-fixed get">
<div class="am-u-md-9">
  <article class="blog-main">
    <h3 class="am-article-title blog-title">
      <a href="#">News at glance</a>
    </h3>
    <h4 class="am-article-meta blog-meta">by <a href="">open</a> posted on 2014/06/17 under <a href="#">字体</a></h4>

    <div class="am-g blog-content">
      <div class="am-u-lg-6">
      <div data-am-widget="slider" class="am-slider am-slider-b2" data-am-slider=\'{&quot;animation&quot;:&quot;slide&quot;,&quot;slideshow&quot;:false}\' >
        <ul class="am-slides">
          <li>
              <img src="http://s.amazeui.org/media/i/demos/bing-1.jpg">
          </li>
          <li>
              <img src="http://s.amazeui.org/media/i/demos/bing-2.jpg">
          </li>
          <li>
              <img src="http://s.amazeui.org/media/i/demos/bing-3.jpg">
          </li>
          <li>
              <img src="http://s.amazeui.org/media/i/demos/bing-4.jpg">
          </li>
        </ul>
      </div>

      </div>
    </div>
    <div class="am-g">
      <div class="am-u-sm-6">
        <p>看著自己的作品，你的喜悅之情溢於言表，差點就要說出我要感謝我的父母之類的得獎感言。但在你對面的客戶先是一點表情也沒有，又瞬間轉為陰沉，抿了抿嘴角冷冷的說……</p>

        <p>「我要一種比較跳的感覺懂嗎？」</p>
      </div>
    </div>
  </article>
  
</div>

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