#include <stdio.h>


int getint(){
int n;
scanf("%d",&n);
return n;
}

int n;
int main(){
	n=getint();
	int ans[2][2]={{3,1},{2,1}};
	int cur[2][2]={{1,1},{1,0}};
	
	int k=n-3;
	k=10;
	while(k){
		int a1,a2,a3,a4;
		if(k%2==1){
			a1=cur[0][0]*ans[0][0]+cur[0][1]*ans[1][0];
			a2=cur[0][0]*ans[0][1]+cur[0][1]*ans[1][1];
			a3=cur[1][0]*ans[0][0]+cur[1][1]*ans[1][0];
			a4=cur[1][0]*ans[0][1]+cur[1][1]*ans[1][1];
			ans[0][0]=a1;
			ans[0][1]=a2;
			ans[1][0]=a3;
			ans[1][1]=a4;
		}
		a1=cur[0][0]*cur[0][0]+cur[0][1]*cur[1][0];
		a2=cur[0][0]*cur[0][1]+cur[0][1]*cur[1][1];
		a3=cur[1][0]*cur[0][0]+cur[1][1]*cur[1][0];
		a4=cur[1][0]*cur[0][1]+cur[1][1]*cur[1][1];
		cur[0][0]=a1;
		cur[0][1]=a2;
		cur[1][0]=a3;
		cur[1][1]=a4;
		
		k = k/2;
	}
	printf("20373444\n");
	int res = ans[0][0];
	printf("%d\n", res);
	printf("%d\n", 678);
	printf("%d\n", res+(res - res));
	printf("%d\n", res / res -10 + res * (res / 2 + (res+1)%122)/ (res / res + 2) + (+(-(+(-res))))*(res % ( res - res + 2*(res/res))));
	printf("%d\n", res - res - 2 * res / 342 + +22234 - 321);
	printf("very weak points\n");
	printf("very weak points\n");
	printf("very weak points\n");
	printf("very weak points\n");
	return 0;
}