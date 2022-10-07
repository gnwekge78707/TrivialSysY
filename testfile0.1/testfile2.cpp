#include <stdio.h>

int getint(){
int n;
scanf("%d",&n);
return n;
}
int satisfyReqSub(int ad1[], int ad2[2][2], int ck) {
	return ck;
}

void satisfyReq() {
	int unaryop = 0;
	int unaryopr = +-+unaryop;
	const int constexp1 = 1, constexp2 = 2;
	const int constexpc= constexp1 + constexp2;;
	const int carr[3] = {1,2,3};
	const int carrr[2][2] = {{1,2},{1,1}};
	int funcArr[2][2] = {{4,3},{0,0}};
	/*constexpc = constexp1 + constexp2;
	
	.
	*/
	;;;;;
	int tempa = satisfyReqSub(funcArr[0], funcArr, 0);
	int tempb = satisfyReqSub(funcArr[1], funcArr, funcArr[0][0]);
	if (0 && satisfyReqSub(funcArr[0], funcArr, 0)) {
		;
	}
	if (1 || satisfyReqSub(funcArr[1], funcArr, funcArr[0][0])) {
		;
	}

}
int a[10000],al[10000],ap[10000],vis[10000],m,n;
int flag;
bool dfs(int u){
	flag++;
	if(flag==n)return 1;
	int p=ap[u];
	while(p!=0){
		if(!vis[a[p]]){
			vis[a[p]]=1;
			if(dfs(a[p]) || 0){
			return 1;
			}
			vis[a[p]]=0;
		}
		p=al[p];
	}
	return 0;
}

int main(){
	flag=0;
	int an=0,x,y;
	n=getint();m=getint();
	int i=0;
	while(i<1000) {
	ap[i]=0;
	i++;
	}
	i=1;
	satisfyReq();
	while(i<=m){
		x=getint();
		y=getint();
		vis[x]=0;
		vis[y]=0;
		an++;a[an]=y;al[an]=ap[x];ap[x]=an;
		an++;a[an]=x;al[an]=ap[y];ap[y]=an;
		i++;
	}
	int ans=0;
	int res = dfs(1);
	printf("20373444\n");
	printf("%d\n", res);
	printf("%d\n", res % 2);
	printf("%d\n", res * 2);
	printf("%d\n", res / 2);
	printf("%d\n", res - 2);
	printf("very weak points\n");
	printf("very weak points\n");
	printf("very weak points\n");
	printf("very weak points\n");
	return 0;
}