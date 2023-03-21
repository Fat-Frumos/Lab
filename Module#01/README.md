# Java External Lab
## Git 
### Task 1: Branching strategy 

#### Commands for git task:
##### Task#01:
`git init`\
`vi README.md`\
`git status`\
`git add . `\
`git commit -m "created readme"`\
`git checkout -b develop`\
`git checkout -b git`\
`git checkout -b git#0`\
`git show-branch`\
`vi README.md`\
`git status`\
`git add README.md`\
`git diff --cached`\
`git commit -m "commit changes in file readme"`\
`git checkout git`\
`git merge git#0 --no-ff`\
`gitk&`\
`git show-branch`\
`git log --all --graph --decorate --oneline --simplify-by-decoration`\
`git log --graph --oneline`\
`git checkout develop`\
`git merge git --no-ff`\
`git remote add origin https://github.com/Fat-Frumos/Lab.git`

`git push origin --all`

##### Task#02:

`git checkout git`\
`git checkout -b git#01`\
`git checkout -b git#02`\
`git checkout git01`

Create firstFile and commit

`for i in {1..10}; do echo "$i. The new line" >> firstFile.txt; done`\
`git add firstFile.txt`\
`git commit -m "Added firstFile.txt file with 10 lines."`

Create secondFile and commit

`for i in {1..10}; do echo "$i. The new line" >> secondFile.txt; done`\
`git add secondFile.txt`\
`git commit -m "Added secondFile.txt file with 10 lines"`\
`git log --all --graph --decorate --oneline --simplify-by-decoration`

Update firstFile in two branches

`git checkout git#2`\
`vi secondFile.txt`\
`git add secondFile.txt`\
`git commit -m "Update and commit any two lines in secondFile.txt"`\
`git checkout git#1`\
`vi secondFile.txt`\
`git add secondFile.txt`\
`git commit -m "Update and commit the same 2 lines with the different info in secondFile.txt"`

Merge branch git#2 to git#1

`git checkout git_2`\
`git merge git_1`

Resolve conflict

`git grep '<<<<<<<'`\
`vi secondFile.txt`\
`git add secondFile.txt`\
`git commit -m "Update and commit any four lines in secondFile.txt"`

Merge branches using format patch and cherrypick

`git checkout git#1`\
`vi firstFile.txt`\
`git commit -m "Update and commit firstFile.txt file, modify two line"`\
`vi firstFile.txt`\
`git commit -m "Update and commit firstFile.txt file, modify another two lines"`

`git log --oneline`\
`git format-patch bcc3a2b --stdout > file.patch`

`git checkout git#2`\
`git apply git#2`\
`git cherry-pick 7751b15`

 Concatenate the last two commits

`git reset --soft HEAD~2`\
`git commit -m "Updated firstFile.txt, modified four lines"`\
`for i in {1..5}; do echo "$i. The new line" >> thirdFile.txt; done`\
`git add thirdFile.txt`\
`git commit --amend --author "Pasha <lokankara@i.ua>" --date "2022-03-21 14:00:00" -m "Updated firstFile.txt, modified four lines and added thirdFile.txt"`
 
 Revert changes of the last one

`git revert HEAD`\
`for i in {1..5}; do echo "$i. The new line" >> thirdFile.txt; done`\
`git add thirdFile.txt`\
`git commit -m "Create thirdFile.txt file"`

Removes all changes of the last two commits

`git reset --hard HEAD~2`\

Synchronize git#1 and git#2 with a remote repository.

`git push origin git#1`\
`git push origin git#2`

`cd ..`\
`mkdir folder2`\
`cd folder2`\
`git clone https://github.com/Fat-Frumos/Lab.git`
\
`git checkout git#1`\
`vi firstFile.txt`\
`git add firstFile.txt`\
`git commit -m "Update firstFile.txt file, modify two line"`\
`git push origin git#1`

Work with command stash

`cd ..`\
`cd folder1`\
`git checkout git#1`\
`vi firstFile.txt`\
`git add firstFile.txt`\
`git status`\
`git stash save "Changes"`\
`git pull git#1`\
`git commit --amend --no-edit`\
`git push origin git#1`\
`git stash apply stash@{0}`\

##### Task#01 with screenshots:

1. Create empty git repository with ***git init***

![](https://raw.githubusercontent.com/Fat-Frumos/Cars/master/media/image1.png)

2. Create and modify README.md with ***vi README.md***

![](https://raw.githubusercontent.com/Fat-Frumos/Cars/master/media/image2.png)

3. Check the progress  ***git status*** add to index stage ***git add .***

![](https://raw.githubusercontent.com/Fat-Frumos/Cars/master/media/image3.png)

4. Commit using and  ***git commit -m "created readme"***

![](https://raw.githubusercontent.com/Fat-Frumos/Cars/master/media/image4.png)

5. Create **develop** branch, **git_task** branch from **develop**, and **git#0** branch from **git_task** with  ***git checkout -b***

![](https://raw.githubusercontent.com/Fat-Frumos/Cars/master/media/image5.png)

6. Update README.md, and add to index stage using ***vi README.md***, ***git add README.md***

![](https://raw.githubusercontent.com/Fat-Frumos/Cars/master/media/image6.png)

7. View staged changes with ***git diff --cached***, and commit ***git commit -m "added new lines to readme"***

![](https://raw.githubusercontent.com/Fat-Frumos/Cars/master/media/image7.png)

8. Merge changes from git#0 branch with ***git merge git_0 --no-ff*** command. 

![](https://raw.githubusercontent.com/Fat-Frumos/Cars/master/media/image8.png)

View latest changes using ***git show-branch*** and ***gitk&***

![](https://raw.githubusercontent.com/Fat-Frumos/Cars/master/media/image9.png)

9. Merge changes to get commit to master (through develop) branch from git#0 branch with ***git merge develop --no-ff*** command. 

![](https://raw.githubusercontent.com/Fat-Frumos/Cars/master/media/image10.png)

10. View changes in tree using ***git show-branch***, ***git log --all --graph --decorate --oneline --simplify-by-decoration***, and ***git log --graph --oneline --all***

![](https://raw.githubusercontent.com/Fat-Frumos/Cars/master/media/image11.png)

