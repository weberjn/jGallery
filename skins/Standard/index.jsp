<%@ taglib uri="http://www.jwi.de/jGallery/taglib" prefix="jg" %>


<% /*
  import se.datadosen.util.*;
  // If meta.properties exists in the image directory and contains
  // an alternative title, use it instead of the directory name
  File propsFile = new File(imageDirectory, "meta.properties");
  if (propsFile.exists()) {
    Properties props = IO.readPropertyFile(propsFile);
    String newTitle = props.get("title");
    if (newTitle != null) title = newTitle;
  }
*/
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=${folder.textEncoding}" />
<title>${folder.title}</title>
<link href="${folder.stylePath}" rel=stylesheet>
</head>

<body id="index">

<table width="100%">
<tr class="head">
<td class="number" width="10%">
<jg:if exists="${folder.totalIndexes}">${folder.indexNum}/${folder.totalIndexes}</jg:if>
</td>
<td></td>
<td class="navigation" width="20%">

<!-- Link to parent index, if any -->
<jg:if exists="${folder.parentIndexPage}">
<a href="${folder.parentIndexPage}"><img src="${folder.resPath}/up.gif" border=0 alt="<jg:text>up</jg:text>"></a>
</jg:if>

<!-- Create navigation buttons if more than one index page -->
<jg:if exists="${folder.totalIndexes}">
	<!-- Previous button -->
	<jg:if exists="${folder.previousIndexPage}">
		<a href="${folder.previousIndexPage}"><img src="${folder.resPath}/previous.gif" alt="<jg:text>previousPage</jg:text>" border=0></a>
	</jg:if>
	<jg:else>
		<img src="${folder.resPath}/previous_disabled.gif" alt="<jg:text>atFirstPage</jg:text>">
	</jg:else>

	<!-- Next button -->
	<jg:if exists="${folder.nextIndexPage}">
		<a href="${folder.nextIndexPage}"><img src="${folder.resPath}/next.gif" alt="<jg:text>nextPage</jg:text>" border=0></a>
	</jg:if>
	<jg:else>
		<img src="${folder.resPath}/next_disabled.gif" alt="<jg:text>atLastPage</jg:text>">
	</jg:else>
</jg:if>

</td>
</tr>
</table>

<!-- Include header.inc from image directory if present -->
<jsp:include page="header.inc" />

<h1>${folder.title}</h1>
<!-- Iterate through images and produce an index table -->
<table>
<jg:rowiterator>
	<tr>
	<jg:coliterator>
		<td width="${folder.maxThumbWidth}" valign="bottom">
			<a href="${image.closeupPath}">
				<jg:if exists="${image.iconPath}">
					<!-- No frames around icons like folders and movie files -->
					<img src="${image.iconPath}" width="${image.thumbWidth}" height="${image.thumbHeight}" border=0><br>
				</jg:if>
				<jg:else>
					<img class="image" src="${image.thumbPath}" width="${image.thumbWidth}" height="${image.thumbHeight}" border=0><br>
				</jg:else>
				<small>${image.label}</small>
			</a>
		</td>
	</jg:coliterator>
	</tr>
</jg:rowiterator>
</table>

<jg:if exists="${folder.comment}">
<p>
${folder.comment}
</jg:if>

<!-- Include footer.inc from image directory if present -->
<jsp:include page="footer.inc" />

<p>
	<a href="${folder.generatorurl}" target="_blank"><small>${folder.generator}</small></a>
	
	<jg:if exists="${folder.counter}">
		<small>folder viewed ${folder.counter} times</small>
	</jg:if>
</p>
</body>
</html>
