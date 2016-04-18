<html>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="css/amazeui.min.css"/>
  <link rel="stylesheet" href="css/my.css">
  <?php $query = $_POST['query'];
  if (empty($query)){
  echo '
  <body>
      <table width="500" height="70" border="0" align="center"  cellpadding="0" cellspacing="0">
      <form name="form1" action="index.php" method="POST" >
      <tr><td><h2>Flash ChemLit</h2></td></tr>
      <tr>
          <td width="2000" height="30" align="center"><p><input type="text" name="query" class="am-form-field am-radius"/></p></td>
      </tr>
      <tr>
          <td height="25" align="center"><input class="submit_btn" id="submit_btn" type="submit" name="submit" value="Search"></td>
      </tr>
      </table>
      <table width="500" height="25" border="0" align="center" cellpadding="0" cellspacing="0">
      </form>
      </table>
      <footer class="blog-footer">
        <p>Last Updated: '; $a=filemtime("cache.txt");
              echo date("Y-m-d H:i:s",$a);echo '<br/>
          <small>&#169; 2016 HANSI MOU ALL RIGHTS RESERVED</small>
        </p>
      </footer>
  </body>
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
      <input type="text" name="query" class="am-form-field am-input-sm" value="';echo $query;echo '" size="55">
      <button type="submit" class="am-btn am-btn-primary am-btn-sm">SEARCH</button>
    </div>
  </form>
  
</div>
</header>';
echo '
<div class="am-g am-g-fixed blog-g-fixed get">
<div class="am-u-md-8">
  <article class="blog-main">
    <h3 class="am-article-title blog-title">
      <a href="#">Google fonts 的字體（display 篇）</a>
    </h3>
    <h4 class="am-article-meta blog-meta">by <a href="">open</a> posted on 2014/06/17 under <a href="#">字体</a></h4>

    <div class="am-g blog-content">
      <div class="am-u-lg-7">
        <p><!-- 本demo来自 http://blog.justfont.com/ -->你自信滿滿的跟客戶進行第一次 demo。秀出你精心設計的內容時，你原本期許客戶冷不防地掉下感動的眼淚。</p>

        <p>因為那實在是太高級了。</p>

        <p>除了各項基本架構幾乎完美無缺之外，內文是高貴的，有著一些距離感的，典雅的襯線字體。不是 Times New
          Roman，而是很少有人見過的，你精心挑選過的字體，凸顯你品味的高超。而且它並沒有花上你與業主一毛錢，或許這也非常重要。</p>
      </div>
      <div class="am-u-lg-5">
        <p><img src="http://f.cl.ly/items/451O3X0g47320D203D1B/不夠活潑.jpg"></p>
      </div>
    </div>
    <div class="am-g">
      <div class="am-u-sm-12">
        <p>看著自己的作品，你的喜悅之情溢於言表，差點就要說出我要感謝我的父母之類的得獎感言。但在你對面的客戶先是一點表情也沒有，又瞬間轉為陰沉，抿了抿嘴角冷冷的說……</p>

        <p>「我要一種比較跳的感覺懂嗎？」</p>
      </div>
    </div>
  </article>

  <hr class="am-article-divider blog-hr">

  <article class="blog-main">
    <h3 class="am-article-title">
      <a href="#">身邊的字體: Arial (上)</a>
    </h3>
    <h4 class="am-article-meta blog-meta">by <a href="">ben</a> posted on 2014/06/17 under <a href="#">javascript</a>
    </h4>

    <div class="am-g blog-content">
      <div class="am-u-lg-7">
        <p><!--本demo文字来自 http://blog.justfont.com/--> 这次要介绍的是大家似乎都狠熟悉却又狠陌生的字体：Arial。不只是对 Typography
          特别有兴趣的人、碰过排版的人，就算毫无接触，只要打开过电脑的字型选单，应该都有看过这个字型吧。尤其它还是以 A 开头，总是会出现在选单最前面。</p>

        <p>Arial 常常跟 Helvetica 搞混，也常被当作是没有 Helvetica 时的替代字体使用。事实上 Arial 确实就是故意做得跟 Helvetica 狠相似，连每个字母的宽度都刻意做得一模一样。</p>
      </div>
      <div class="am-u-lg-5">
        <p><img src="https://farm3.staticflickr.com/2917/14186214720_5d0b8ca2e3_b.jpg"></p>
      </div>
    </div>
    <div class="am-g">
      <div class="am-u-sm-12">
        <p>在欧美的排版业界中，使用 Arial 的作品意即是「不使用 Helvetica 的作品」，会被认為是设计师对字体的使用没有概念或是太容易妥协，基本上我大致也是同意。</p>

        <p>因為 Helvetica 只有 Mac 上才有內建，Windows 用戶除非花錢買，不然是沒有 Helvetica 能用，所以使用 Arial 的設計師往往被看成是不願意對 Typography
          花錢，專業素養不到家的人。除了在確保網頁相容性等絕對必需的情況外，幾乎可以說是不應該使用的字體。</p>

        <p>但是，在此之前，我們對 Arial 又有多少認識呢？</p>
      </div>
    </div>
  </article>

  <hr class="am-article-divider blog-hr">
  <ul class="am-pagination blog-pagination">
    <li class="am-pagination-prev"><a href="">&laquo; 上一页</a></li>
    <li class="am-pagination-next"><a href="">下一页 &raquo;</a></li>
  </ul>
</div>
</div>
';

  }
  ?>
</html>

