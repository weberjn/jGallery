<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
  <title>jGallery</title>
  <link href="style.css" type="text/css" rel="stylesheet">
  <meta http-equiv="content-type" content="text/html;CHARSET=iso-8859-1">
</head>
<body lang="en">
<%@ page isELIgnored="true" %>
<h1>jGallery<br>
</h1>
<p>jGallery is a web application to display image galleries. Contrary
to <a href="http://www.datadosen.se/jalbum/">JAlbum</a>, which
generates HTML pages offline, jGallery creates HTML at
request time. jGallery uses templates (skins) to define the generated
pages. jGallery's templates are to a high degree compatible with
JAlbum's skins. Get them at <a href="http://jrepository.engblom.org/">http://jrepository.engblom.org/</a>.<br>
</p>
<p>jGallery uses <a
 href="http://www.geocities.com/marcoschmidt.geo/image-info.html">Marco
Schmidt's ImageInfo</a> class and <a
 href="http://www.drewnoakes.com/code/exif/">Drew Noakes</a>' metadata
extraction library.<br>
</p>
<p>jGallery itself is licensed under the <a href="http://www.gnu.org/">Gnu
Public License</a> (GPL).<br>
</p>
<p>Nevertheless, I'd be happy if you <a href="contact.jsp#postcard">send
me</a> a
picture postcard of your place.<br>
</p>
<h2>Contents</h2>
<ul>
  <li><a href="#What_you_need">What you need</a></li>
  <li><a href="#Installation">Installation</a></li>
  <li><a href="#how_jGallery_finds_its_images">URI Addresses of
generated HTML pages</a></li>
  <li><a href="#Configuration">Configuration</a></li>
  <li><a href="#Adapting_a_JAlbum_skin">Adapting a JAlbum skin</a></li>
  <li><a href="#Thanks">Thanks</a></li>
</ul>
<br>
<h3><a name="What_you_need"></a>What you need</h3>
<ul>
  <li>a web container supporting JSP 2.0. At the moment (March 2003)
there is only <a href="http://jakarta.apache.org/">Jakarta Tomcat 5</a>.
  </li>
  <li><a href="http://java.sun.com/j2se/">JDK 1.4.1_02</a> or newer</li>
  <li>folders with images to display ;-)<br>
  </li>
</ul>
<h3><a name="Installation">Installation</a></h3>
<p>
</p>
<h4>For Tomcat 5:</h4>
<p>Unpack the downloaded jGallery-XX.zip to a folder of your liking and
you'll get a folder structure like this:<br>
</p>
<p><img style="width: 162px; height: 147px;" alt="Folderstructure"
 src="folderstructure.gif"><br>
</p>
The inner jGallery is the web application, you have to tell Tomcat
where it is. This is described in Tomca<span style="font-weight: bold;"></span><strong></strong>t
user guide in section Deployer, there in <em>Context descriptors</em>.<br>
<br>
Basically you have to copy the jGallery.xml file found in the jGallery
folder to <br>
<br>
$CATALINA_HOME/conf/[enginename]/[hostname]/<br>
<br>
&nbsp;(on my
machine this is
/jakarta-tomcat-5.0.18/conf/Catalina/localhost/) and adapt
the docBase attribute to the real path of the web application (that is
the parent directory of WEB-INF).<br>
<br>
Afterwards restart Tomcat.<br>
<h3><a name="how_jGallery_finds_its_images">URI Addresses of generated
HTML pages</a></h3>
<br>
For any given image /<strong><em>path</em></strong>/<em>animage</em>.jpg
jGallery will create a html page reachable at <br>
/jGallery/<strong><em>path</em></strong>/<em>animage</em>.html and the
thumbnail page will be at /jGallery/<strong><em>path</em></strong>/<em>index</em>.html.<br>
<br>
If you install jGallery as default context, the /jGallery part will
lack and the above scenario changes to /<strong><em>path</em></strong>/<em>animage</em>.html
and /<strong><em>path</em></strong>/<em>index.html.</em><br>
<br>
<br>
Example:<br>
<br>
For an image deployed in context <span
 style="color: rgb(102, 102, 204);">jsp-examples </span>and reachable
with <br>
<br>
http://localhost:8080/<span style="color: rgb(102, 102, 204);">jsp-examples</span>/<span
 style="color: rgb(0, 153, 0);">jsp2/jspx/textRotate</span>.jpg<br>
<br>
jGallery will create an html page at <br>
<br>
http://localhost:8080/<span style="color: rgb(102, 102, 204);">jsp-examples</span>/<span
 style="color: rgb(0, 153, 0);">jsp2/jspx/textRotate</span>.html<br>
<br>
and a thumbnail page at <br>
<br>
http://localhost:8080/<span style="color: rgb(102, 102, 204);">jsp-examples</span>/<span
 style="color: rgb(0, 153, 0);">jsp2/jspx/</span>index.html<br>
<br>
<span style="font-weight: bold;"></span>
<h4>Images served by another server</h4>
There is a special case for images that are not below the Tomcat
hierarchy, but are served by Apache or even might be on another
webserver. If the browser requests a page at /jGallery/<strong><em>folder/path</em></strong>/<em>anotherimage</em>.html
jGallery will get the real location of the images from a file
/jGallery/WEB-INF/web.properties.<br>
This file must contain an entry <strong><em>folder=URL</em></strong>
to describe where the images are.<br>
<br>
Be careful not to use <strong><em>folder </em></strong>keys that
might be Tomcat ressources, too.<br>
<br>
For jGallery to be able to generate in index.html page, there must be a
file images.txt in the images folder.<br>
<br>
In order to get image dimensions and Exif information for images served
in this way, jGallery first reads images.txt for this web folder via
the net (!) and then each image. Information about the images is cached
in /jGallery/WEB-INF/cache/* so a given image is only read once.&nbsp;
It is assumed that an image with a given name does not change.<br>
<br>
Example:<br>
<br>
With web.properties containing an entrie <br>
<br>
<strong><em></em></strong><span style="color: rgb(102, 102, 204);">myApacheImages</span>=<span
 style="color: rgb(0, 153, 0);">http://localhost:80/images/</span><br>
<br>
and the browser requesting <br>
<br>
/jGallery/<strong><em></em></strong><span
 style="color: rgb(102, 102, 204);">myApacheImages</span>/holiday2004/CRW_9081.html<br>
<br>
jGallery generates a html page that references the image at <br>
<br>
<span style="color: rgb(0, 153, 0);">http://</span><span
 style="color: rgb(0, 153, 0);">localhost:80/images/</span>holiday2004/CRW_9081.jpg<br>
<br>
A request to <br>
<br>
/jGallery/<strong><em></em></strong><span
 style="color: rgb(102, 102, 204);">myApacheImages</span>/holiday2004/index.html<br>
<br>
would display a thumbnail page with all images listed in <br>
<br>
<span style="color: rgb(0, 153, 0);">http://</span><span
 style="color: rgb(0, 153, 0);">localhost:80/images/</span>holiday2004/images.txt<br>
<br>
It is also possible to reference a remote folder on the same machine
with a file url, e.g.<br>
<br>
myApacheFiles=file:///D:/apache/htdocs/images<br>
<br>
In that case there needs to be another entry&nbsp; key<span
 style="font-weight: bold;">.url</span> to get the URL the web server
serves this folder with, e.g.<br>
<br>
myApacheFiles<span style="font-weight: bold;">.url</span>=http://myserver/images<br>
<p></p>
<h3><a name="Configuration">Configuration</a></h3>
<p>
On startup jGallery reads properties from
/jGallery/WEB-INF/jGallery.properties.
These can be overridden with properties jGallery.properties in an image
folder. This file is read on the first access of the image folder, once
for each HTTP session. </p>
<br>
<table style="text-align: left; width: 100%;" border="0" cellspacing="2"
 cellpadding="2">
  <tbody>
    <tr>
      <td style="vertical-align: top; text-align: justify; width: 25%;">skin<br>
      </td>
      <td style="vertical-align: top;">skin is the name of the folder
to use for index and slide templates in jGallery/skins/<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top; text-align: justify; width: 25%;">style<br>
      </td>
      <td style="vertical-align: top;">style+".css" is the name of the
CSS to use in jGallery/skins/&lt;selected skin&gt;/styles/<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top; text-align: justify; width: 25%;">index.columns</td>
      <td style="vertical-align: top;">number of columns to display on
an index page<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top; text-align: justify; width: 25%;">index.rows</td>
      <td style="vertical-align: top;">number of rows to display on an
index page</td>
    </tr>
    <tr>
      <td style="vertical-align: top; text-align: justify; width: 25%;">sortingOrder<br>
      </td>
      <td style="vertical-align: top;">sorting order for images in a
folder: none, name, filedate, exifdate<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top; text-align: justify; width: 25%;">thumbnails.dir<br>
      </td>
      <td style="vertical-align: top;">folder name of thumbnails,
relativ to images<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top; text-align: justify; width: 25%;">thumbnails.create<br>
      </td>
      <td style="vertical-align: top;">decides, if jGallery creates
thumbs: true, false<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top; text-align: justify; width: 25%;">thumbnail.size<br>
      </td>
      <td style="vertical-align: top;">size in pixels of the square the
thumb fits in<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top; text-align: justify; width: 25%;">thumbnail.quality<br>
      </td>
      <td style="vertical-align: top;">float value for JPEG Quality (0
.. 1.0)<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top; text-align: justify; width: 25%;"><br>
      </td>
      <td style="vertical-align: top;"><br>
      </td>
    </tr>
  </tbody>
</table>
<br>
jGallery.properties can define user variables. These are of the form
variable.&lt;<span
 style="color: rgb(102, 102, 204); font-style: italic;">name</span>&gt;=value.<br>
In the JSPs they used as ${folder.variables["<span
 style="color: rgb(102, 102, 204); font-style: italic;">name</span>"]}.
User variables can be overridden in the same way as normal properties.<br>
<br>
e.g. define as<br>
<br>
variable.<span style="color: rgb(102, 102, 204); font-style: italic;">copyright</span>=(c)
2004<br>
<br>
and use as<br>
<br>
${folder.variables["<span
 style="color: rgb(102, 102, 204); font-style: italic;"><span
 style="color: rgb(102, 102, 204); font-style: italic;">copyright</span></span>"]}<br>
<br>
<p></p>
<h3><a name="Adapting_a_JAlbum_skin">Porting a JAlbum skin</a></h3>
<p>A skin consists of JSP 2.0 files. In order to make porting JAlbum
skins as easy as possible, jGallery uses variables of same name and
semantic as JAlbum, but variables can only be referenced from an
object. jGallery calls pages with the current Folder and Image
object bound to the request. From there you can reference all
variables. For example, instead of JAlbum's $indexNum use
${folder.indexNum} in JSP 2.0 EL syntax, for the thumbWidth image
information use ${image .thumbWidth}. Exif information is in an
Exif object referenced from the image (e.g. ${image.exif.originalDate}
). <br>
</p>
<p>The image object is bound to the image currently displayed on a
slide or, within a coliterator tag, the current image.<br>
</p>
<p>Generally the less &lt;% %&gt; Java scriptlets are in the JAlbum
HTML templates, the easier you can port a skin. If there are no
scriptlets, the skin can be ported almost automatically.<br>
</p>
<p>Steps to do:
</p>
<ul>
  <li>unpack the skin to /jGallery/skins</li>
  <li>for index.htt and slide.htt run <code>java
de.jwi.jgallery.util.VariablesTransformer &lt;htt file&gt;</code></li>
  <li>replace the Scriptlets. Simple things like <code>&lt;%=
aJalbumVar mathop aNumber %&gt;</code> can be replaced with a JSP 2.0
EL expression like <code>${folder.aJalbumVar mathop aNumber}</code></li>
  <li>rename the *.htt.jsp to *.jsp</li>
  <li>run your gallery and try to fix other errors, JSP 2.0 is very
strict with matching tags and matching double quotes.</li>
</ul>
<h4>How I ported JAlbums Standard skin:</h4>
<ul>
  <li>ran VariablesTransformer </li>
  <li>replaced all &lt;%=title%&gt; with ${folder.title}</li>
  <li>replaced &lt;%= new File(imageDirectory, label+".txt") %&gt; with
${image.label}.txt</li>
  <li>On the slide page, removed the ../ from
"../${folder.indexPage}" (jGallery only works with absolute paths).<br>
  </li>
</ul>
<br>
That's all!<br>
<br>
<br>
<h3><a name="Thanks">Thanks to<br>
</a></h3>
<ul>
  <li><a href="http://www.geocities.com/marcoschmidt.geo/contact.html">Marco
Schmidt</a> for the&nbsp; <a
 href="http://www.geocities.com/marcoschmidt.geo/contact.html">ImageInfo</a>
class and valuable information about java imaging<br>
  </li>
  <li><a href="http://drewnoakes.com/">Drew
Noakes</a> for the Metadata extractor library</li>
  <li><a href="http://www.datadosen.se/jalbum/">David Ekholm</a> for
the concept of album skins and variables</li>
  <li><a href="http://www.mje.de/">Matze</a> for discussing concepts of
jGallery</li>
  <li>Carsten for testing<br>
  </li>
  <li>and the creators of <a href="http://java.sun.com/">Java</a>, <a
 href="http://jakarta.apache.org/">Tomcat</a> and <a
 href="http://www.eclipse.org/">Eclipse</a>.<br>
  </li>
</ul>
<a name="postcard"></a>And, if you want to thank me, <a
 href="contact.jsp#postcard">send me a picture postcard.</a><br>
<p class="lastmod">Last modification 2004-03-06</p>
<p class="copyright">Copyright &copy; 2004 <a href="contact.jsp">J&uuml;rgen
Weber</a></p>
</body>
</html>
