#!/usr/local/bin/php



<HTML><HEAD>
<SCRIPT>
function getHTMLInputText(){
  return document.forms[0].elements[0].value;
}

function setHTMLInputText(s){
  document.forms[1].elements[1].value = s;
  tmpvar=s;
}

</SCRIPT></HEAD><BODY>

<FORM>
  <INPUT TYPE=text SIZE=40>
</FORM>


<applet code=diet.server.experimentmanager.EMStarterApplet.class 
      archive="/jarfile/chattool.jar"
      width=160 height=40 MAYSCRIPT> 
      <param name="assignmentID"  value="<?php echo $assignment_id ?>"
      <param name="participantID" value="<?php echo $turk_id; ?>"
      
</applet>
b
<br>
<br>
a
<br>
<br>


<form id="mturk_form" method="POST" action="http://workersandbox.mturk.com/mturk/externalSubmit" align=right>
      <input type="hidden" id="assignmentId" name="assignmentId" value="<?php echo $assignment_id ?>">
      <input type="visible" id = "codeval" name = "codeval" value ="VALUE" readonly>
      <input id="submitButton" type="submit" name="Submit" value="Complete HIT">
</form>

<?php echo $assignment_id ?><br>
<?php echo $turk_id ?>

a
<hr>





</html>