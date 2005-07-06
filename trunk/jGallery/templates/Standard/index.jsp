
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


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
<c:if test="${!empty folder.totalIndexes}">${folder.indexNum}/${folder.totalIndexes}</c:if>
</td>
<td></td>
<td class="navigation" width="20%">

<!-- Link to parent index, if any -->

<c:if test="${!empty folder.parentIndexPage}"> 
<a href="${folder.parentIndexPage}"><img src="${folder.resPath}/up.gif" border=0 alt="up"></a>
</c:if>

<!-- Create navigation buttons if more than one index page -->
<c:if test="${!empty folder.totalIndexes}"> 
	<!-- Previous button -->
	
	
	<c:choose> 
		<c:when test="${!empty folder.previousIndexPage}">
			<a href="${folder.previousIndexPage}"><img src="${folder.resPath}/previous.gif" alt="previous page>" border=0></a>
		</c:when> 
		<c:otherwise> 
		<img src="${folder.resPath}/previous_disabled.gif" alt="at first page">
		</c:otherwise>
	</c:choose>

	<!-- Next button -->
	
	<c:choose> 
		<c:when test="${!empty folder.nextIndexPage}">
			<a href="${folder.nextIndexPage}"><img src="${folder.resPath}/next.gif" alt="next page" border=0></a>
		</c:when> 
		<c:otherwise>
			<img src="${folder.resPath}/next_disabled.gif" alt="at last page">
		</c:otherwise>
	</c:choose>
</c:if>

</td>
</tr>
</table>

<!-- Include header.inc from image directory if present -->
<jsp:include page="header.inc" />

<h1>${folder.title}</h1>
<!-- Iterate through images and produce an index table -->
<table>

<c:forEach var="row" items="${folder.imageRows}">
	<tr>
	<c:forEach var="image" items="${row}">
		<td width="${folder.maxThumbWidth}" valign="bottom">
			<a href="${image.closeupPath}">
				<c:choose> 
				<c:when test="${!empty image.iconPath}">
					<!-- No frames around icons like folders and movie files -->
					<img src="${image.iconPath}" width="${image.thumbWidth}" height="${image.thumbHeight}" border=0><br>
				</c:when>
				<c:otherwise>
					<img class="image" src="${image.thumbPath}" width="${image.thumbWidth}" height="${image.thumbHeight}"
					title="click to see large view"
					border="0"><br>
				</c:otherwise>
				</c:choose> 
				<small>${image.label}</small>
			</a>
		</td>
	</c:forEach>
	</tr>
</c:forEach>
</table>

<c:if test="${!empty folder.comment}">
<p>
${folder.comment}
</c:if>

<!-- Include footer.inc from image directory if present -->
<jsp:include page="footer.inc" />

<p>
	<a href="${folder.generatorurl}" target="_blank"><small>${folder.generator}</small></a>
	
	<c:if test="${!empty folder.counter}">
		<small>folder viewed ${folder.counter} times</small>
	</c:if>
</p>
</body>
</html>
