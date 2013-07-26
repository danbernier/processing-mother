<?php ob_start() ?>
<?php
if ($_GET['randomId'] != "NgwQAFkSn_bjC93uJ3cO05ITREAJoXyx9iuAB26DcBvxcFbF3LUNlzEKmwlVYFgf2CHMF8IhN6zpe9Bebqjai1osTOiJErEZBfSE80e_OOR26ekpt9Dl6UIkMjouT5Dvk1tfwEdYWayZVIyeHprPO8mm9Vp29WLCu8rANl1QfS0l8xTIiAX00dv74OjScUc4m_M0IDv33C4qCPr7AxvYpeqonjVfAcFyISIb2SmH0pDsbroFHz0Mk_GMQ2oehkT8") {
    echo "Access Denied";
    exit();
}
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Editing index.html</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">body {background-color:threedface; border: 0px 0px; padding: 0px 0px; margin: 0px 0px}</style>
</head>
<body>
<div align="center">

<div id="saveform" style="display:none;">
<form METHOD="POST" name=mform action="http://www.onar3d.com:2082/frontend/x3/filemanager/savehtmlfile.html">
    <input type="hidden" name="charset" value="ISO-8859-1">
    <input type="hidden" name="baseurl" value="http://www.onar3d.com/public_html/mother/">
    <input type="hidden" name="basedir" value="/home/onar3d9/public_html/">
    <input type="hidden" name="udir" value="/home/onar3d9/public_html/mother">
    <input type="hidden" name="ufile" value="index.html">
    <input type="hidden" name="dir" value="%2fhome%2fonar3d9%2fpublic_html%2fmother">
    <input type="hidden" name="file" value="index.html">
    <input type="hidden" name="doubledecode" value="1">
<textarea name=page rows=1 cols=1></textarea></form>
</div>
<div id="abortform" style="display:none;">
<form METHOD="POST" name="abortform" action="http://www.onar3d.com:2082/frontend/x3/filemanager/aborthtmlfile.html">
    <input type="hidden" name="charset" value="ISO-8859-1">
    <input type="hidden" name="baseurl" value="http://www.onar3d.com/public_html/mother/">
    <input type="hidden" name="basedir" value="/home/onar3d9/public_html/">
    <input type="hidden" name="dir" value="%2fhome%2fonar3d9%2fpublic_html%2fmother">
        <input type="hidden" name="file" value="index.html">
    <input type="hidden" name="udir" value="/home/onar3d9/public_html/mother">
    <input type="hidden" name="ufile" value="index.html">

        </form>
</div>
<script language="javascript">
<!--//

function setHtmlFilters(editor) {
// Design view filter
editor.addHTMLFilter('design', function (editor, html) {
        return html.replace(/\<meta\s+http\-equiv\="Content\-Type"[^\>]+\>/gi, '<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />');
});

// Source view filter
editor.addHTMLFilter('source', function (editor, html) {
        return html.replace(/\<meta\s+http\-equiv\="Content\-Type"[^\>]+\>/gi, '<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />');
});
}

// this function updates the code in the textarea and then closes this window
function do_save() {
    document.mform.page.value = WPro.editors[0].getValue();
	document.mform.submit();
}
function do_abort() {
	document.abortform.submit();
}
//-->
</script>
<?php
// make sure these includes point correctly:
include_once ('/usr/local/cpanel/base/3rdparty/wysiwygPro/wysiwygPro.class.php');

// create a new instance of the wysiwygPro class:
$editor = new wysiwygPro();

$editor->registerButton('save', 'Save',
        'do_save();', '##buttonURL##save.gif', 22, 22,
        'savehandler'); 
$editor->addRegisteredButton('save', 'before:print' );
$editor->addJSButtonStateHandler ('savehandler', 'function (EDITOR,srcElement,cid,inTable,inA,range){ 
        return "wproReady"; 
        }'); 


$editor->registerButton('cancel', 'Cancel',
        'do_abort();', '##buttonURL##close.gif', 22, 22,
        'cancelhandler'); 
$editor->addRegisteredButton('cancel', 'before:print' );
$editor->addJSButtonStateHandler ('cancelhandler', 'function (EDITOR,srcElement,cid,inTable,inA,range){ 
        return "wproReady"; 
        }'); 
$editor->theme = 'blue'; 
$editor->addJSEditorEvent('load', 'function(editor){editor.fullWindow();setHtmlFilters(editor);}');

$editor->baseURL = "http://www.onar3d.com/public_html/mother/";

$editor->loadValueFromFile('/home/onar3d9/public_html/mother/index.html');

$editor->registerSeparator('savecan');

// add a spacer:
$editor->addRegisteredButton('savecan', 'after:cancel');

//$editor->set_charset('iso-8859-1');
$editor->mediaDir = '/home/onar3d9/public_html/';
$editor->mediaURL = 'http://www.onar3d.com/';
$editor->imageDir = '/home/onar3d9/public_html/';
$editor->imageURL = 'http://www.onar3d.com/';
$editor->documentDir = '/home/onar3d9/public_html/';
$editor->documentURL = 'http://www.onar3d.com/';
$editor->emoticonDir = '/home/onar3d9/public_html/.smileys/';
$editor->emoticonURL = 'http://www.onar3d.com/.smileys/';
$editor->loadPlugin('serverPreview'); 
$editor->plugins['serverPreview']->URL = 'http://www.onar3d.com/public_html/mother/.wysiwygPro_preview_eacf331f0ffc35d4b482f1d15a887d3b.php?randomId=NgwQAFkSn_bjC93uJ3cO05ITREAJoXyx9iuAB26DcBvxcFbF3LUNlzEKmwlVYFgf2CHMF8IhN6zpe9Bebqjai1osTOiJErEZBfSE80e_OOR26ekpt9Dl6UIkMjouT5Dvk1tfwEdYWayZVIyeHprPO8mm9Vp29WLCu8rANl1QfS0l8xTIiAX00dv74OjScUc4m_M0IDv33C4qCPr7AxvYpeqonjVfAcFyISIb2SmH0pDsbroFHz0Mk_GMQ2oehkT8';
// print the editor to the browser:
$editor->htmlCharset = 'ISO-8859-1';
$editor->urlFormat = 'relative';
$editor->display('100%','450');

?>
</div>
<script>

</script>

</body>
</html>
<?php ob_end_flush() ?>
