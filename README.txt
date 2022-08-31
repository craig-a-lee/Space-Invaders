=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: leecraig
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays - The invaders are stored in 2D arrays as they move across and down
  the game court. 2D arrays made the most logical This features an interesting 
  use of 2D arrays because only invaders at the bottom of their column can shoot 
  at the player.

  2. JUnit Testable Component - The game can operate independently of the GUI so it
  was suitable to test the core game logic through JUnit tests. Tests were run 
  to check varying cases based on the information stored on the game court. These
  tests included cases such as if the user lost by running out of lives, the 
  lowest invader having the same y position as the shooter and if game objects 
  had appropriate responses based on being hit by an attack. 
	
  3. File I/O - The current state of the game court is stored in a .txt file when the use closes 
  the game. This allows the user to quit the game and restart where they quit. The
  file stores the number of lives, number of invaders left and their x positions, 
  the position of the highest row of invaders (even if that row is empty because
  this value is used to calculate the y position of the remaining invaders), the 
  last x position of the player, the health of the shields,  the last registered
  score and the highest ever recorded score

  4. Collections - Due to the fact that multiple attacks will be stored on the 
  screen at the same time, they have the same properties, and they do not have the 
  same square-like set up as the invaders, these attacks need to be stored in collections. 
  The shield objects are also stored in collections. A linked list was used for the attacks because testing and 
  debugging were made simpler when the ordering of the attacks was specified. Maps
  were considered since mapping the invaders to their attacks or player/shooter
  to its attacks  was unnecessary for the game logic. A linked list was also used to 
  temporarily store the attacks to be removed from the main linked list to avoid a ConcurrentModificationException.
  The shields were also stored in a linked list because only three shields 
  present at any given time and so I could use a shield's position in the list to update files and game logic 
  accordingly. This made ordering important. 
  
=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

Shooter class (extends GameObj) - represents the spaceship which is controlled by 
the player. The player can move the shooter to the left and right using the left
and right arrows respectively and can use spacebar to fire attacks at the invader.

Invader class (extends GameObj) - represents invaders which move across and down
the screen as time progresses. The enemy of this game. 

Attack class (extends GameObj) - allows for attacks to be created so that 
both the player and invaders fire attacks which can interact with objects
on the court. These attacks can hit the player, the invaders and the shields.

Shield class (extends GameObj) - stores relevant information such as shield health to 
act as a barrier within the game. It acts both ways meaning that both 
the player and the invaders can damage the barrier.

Gamecourt class (extends JPanel) - where the game actually occurs and where the game logic is
updated. Contains all the variables and methods for the necessary functioning. 

RunSpaceInvader class (extends Runnable) - handles the displaying of the game and its 
buttons on a window, initializes Gamecourt.

GameObj abstract class - contains generalized functions for an object that exists
within my game.

Game class - contains main method to start and run game

Direction class - contains directions which represent how an object is to move
in my game

FileLineIterator class (extends Iterator) - used to iterate though my data file to 
get info to load into my game

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

My biggest stumbling block was probably getting the invaders to randomly 
fire attacks and only the bottom row of invaders at that. I had an overly complex
solution for this which did not even work effectively. I avoided trying to fix
this for a while since I thought it would be hard but when I took a step back
and thought it over, I realized I was overthinking it.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

I think there is a good separation of functionality since modules were created 
to perform specific tasks. I also think encapsulation was maintained. I am not sure
what I would refactor. 

========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
  files/player.png -> http://pixelartmaker.com/art/6a7a884cc5870b0
  files/invader1.png -> https://emojipedia.org/alien-monster/
  files/space1.png -> https://opengameart.org/content/space-backgrounds-0
  
  files/shield1.png, files/shield2.png, file/sheild3.png, files/shield4.png, 
  files/shield5.png ->
  https://www.hiclipart.com/free-transparent-background-png-clipart-fcvvb
  

  
  
