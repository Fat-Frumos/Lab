# Java External Lab
## Git 
### Task 1: Branching strategy 

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
