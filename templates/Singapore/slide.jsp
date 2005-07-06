<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  
   <meta http-equiv="Content-Type" content="text/html; charset=${folder.textEncoding}" />

    <link rel="stylesheet" type="text/css" media="screen" href="${folder.templatePath}/main.css" />


  <title>${image.label}</title>
  <link rel="stylesheet" type="text/css" href="${folder.templatePath}/main.css"/>

 </head>


<body>


<div id="crumb">
You are here:

<c:forEach var="parent" items="${folder.parentFolderList}">

<a href="${parent.url}">${parent.name}</a>

	           &gt;               

</c:forEach>

<a href="${folder.indexPage}">${folder.name}</a>

 &gt;

${image.name}

</div>



<p class="sgTopNavBar">


<c:forEach var="image" items="${image.neighbourImages}">

	<a href="${image.closeupPath}">
	
	<c:choose> 
		<c:when test="${image.iconPath}"> 
			<img class="sgPreviewThumb"
			src="${image.iconPath}" 
			width="${image.thumbWidth / 2}" height="${image.thumbHeight / 2}"
			alt="" />
		</c:when> 
		<c:otherwise> 
			<img class="sgPreviewThumb"
			src="${image.thumbPath}" 
			width="${image.thumbWidth / 2}" height="${image.thumbHeight / 2}" title="click to see large view"
			alt="" />
		</c:otherwise> 
	</c:choose>  
	</a>
	
</c:forEach>

<br />


<c:if test="${!empty folder.previousPage}"> 
    	<a href="${folder.previousPage}">Previous</a> | 
  </c:if> 

  <a href="${folder.indexPage}"  title="${folder.title}">Thumbnails</a>
  
  <c:if test="${!empty folder.nextPage}"> 
    |	<a href="${folder.nextPage}">Next</a> 
  </c:if> 
 
</p>



  
<h1 style="margin-bottom: 0px;">${image.label}</h1>
<c:if test="${!empty folder.variables.copyright}"> 
	<p style="margin-top: 0pt;"> by ${folder.variables.copyright}</p>
</c:if> 







<div class="sgShadow"><table class="sgShadow" cellspacing="0">
  <tr>
    <td class="tl"><img src="${folder.templatePath}/images/blank.gif" width="32" height="16" alt="" /></td>
    <td class="tm"><img src="${folder.templatePath}/images/blank.gif" alt="" /></td>
    <td class="tr"><img src="${folder.templatePath}/images/blank.gif" alt="" /></td>
  </tr>
  <tr>
    <td class="ml"><img src="${folder.templatePath}/images/blank.gif" alt="" /></td>
    <td class="mm">
  
    <img src="${image.imagePath}" class="sgImage"
    	alt="${folder.name}" height="${image.imageHeight}" width="${image.imageWidth}" /> 
  
    </td>
    <td class="mr"><img src="${folder.templatePath}/images/blank.gif" alt="" /></td>
  </tr>
  <tr>
    <td class="bl"><img src="${folder.templatePath}/images/blank.gif" alt="" /></td>
    <td class="bm"><img src="${folder.templatePath}/images/blank.gif" alt="" /></td>
    <td class="br"><img src="${folder.templatePath}/images/blank.gif" width="32" height="32" alt="" /></td>
  </tr>
</table></div>



  <c:if test="${!empty image.exif.flash}"> 
  <div class="footer">
  <p><small>
Date: ${image.exif.originalDate} |
Flash: ${image.exif.flash} |
Focal length: ${image.exif.focalLength} |
Exposure time: ${image.exif.exposureTime} |
Aperture: ${image.exif.aperture} |
ISO: ${image.exif.isoEquivalent} |
Camera model: ${image.exif.cameraModel}
</small></p>
</div>
</c:if>  




<c:if test="${!empty image.counter}"> 
	<p><small>image viewed ${image.counter} times</small></p>
</c:if>  

<div id="footer">
<p> <a href="${folder.generatorurl}" target="_blank"><small>Powered by ${folder.generator}</small></a></p>
</div>

</body>
</html>