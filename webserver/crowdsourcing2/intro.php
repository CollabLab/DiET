#!/usr/local/bin/php

<h3>Instructions</h3>

<ol>
<li>We are looking for fluent speakers of English</li>
<li>You'll first be presented with an experimental consent form; the transcripts from these games will be used to study collaborative problem solving.</li>
<li>Once you click through, you'll go into &quot;Wait for Participant&quot; mode until another player shows up. <span style="color:red;">It might take a while for another player to show up! We suggest opening a new window and doing other things until someone arrives</span></li>
<li>When play begins, you will be shown a task description: you and your partner have to imagine you are in a survival scenario, and you need to work out which objects are useful for your survival
</li>
<li>When you've solved the game or the time is finsished, click the &quot;Task Complete&quot; button to register that the task is complete. You can continue playing and chatting at that point, or you can click <strong>Complete HIT and return to MTurk</strong> at the bottom of the screen.</li>
<li>You will receive a bonus of up to $0.50 for good collaboration with meaningful communication.</li>
<li>Notes:
<ul>
<li>You must communicate with your partner by typing in a chat window</li>
</ul></li>
<li><span style="color:red;">Caution: Players who do not complete the task properly will not be paid. It is vital that you read and understand the task description and follow the instructions.</span></li>
</ol>

<div>
</div>


<p>
<form id="amt_contform" method="POST" action="page2OPTIONS.php">
	<input type="hidden" id="turkId" name="turkId" value="<?php echo $_REQUEST['workerId']; ?>">	
	<input type="hidden" id="assignmentId" name="assignmentId" value="<?php echo $_REQUEST['assignmentId'];  ?>">
	<input id="submitButton" type="submit" name="Begin" value="Begin" 
	<?php 
		if ($_REQUEST['workerId'] == "") {
			echo 'onclick="alert(\'Please accept this HIT before continuing.\'); return false;"';
		}
	?> 
	>
</form>
