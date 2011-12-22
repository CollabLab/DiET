#!/usr/local/bin/php

<h3>Instructions</h3>

<ol>

<li>You'll first be presented with an experimental consent form; the transcripts from these games will be used to study collaborative problem solving.</li>
<li>Once you click through, you'll go into &quot;Wait for Participant&quot; mode until another player shows up. <span style="color:red;">It might take a while for another player to show up! We suggest opening a new window and doing other things until someone arrives</span></li>
<li>When play begins, you'll be presented with a task description: you and your partner are tasked with finding six consecutive cards of the same suit.</li>
<li>When you've solved the game, click the &quot;Task Complete&quot; button to register that the task is complete. You can continue playing and chatting at that point, or you can click <strong>Complete HIT and return to MTurk</strong> at the top of the screen.</li>
<li>You will receive a bonus of up to $0.50 for good collaboration with meaningful communication.</li>
<li>Notes:
<ul>
<li>Once the game begins, you can move around with the arrow keys or the on-screen buttons.</li>
<li>The chat window is near the top of the screen.</li>
<li>You can't see your partner, and your partner can't see you!</li>
<li>The yellow boxes mark cards in your &quot;line of sight&quot;.</li>
</ul></li>
<li><span style="color:red;">Caution: Players who do not complete the task properly will not be paid. It is vital that you read and understand the task description and follow the instructions.</span></li>
<li>Feel free to play multiple times &mdash; especially useful if you get good at the game.</li>
</ol>

<div>
<h3>Annotated game screen</h3>
<img src="pragbot-screenshot-ann.png" width="915" width="655" alt="Annotated game screen" />
</div>


<p>
<form id="amt_contform" method="POST" action="http://alliance.seas.upenn.edu/~muri/cgi-bin/pragbot/classes/PragbotClient.php">
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
