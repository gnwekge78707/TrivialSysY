#include <stdio.h>


int nn,ans;
int getint(){
int n;
scanf("%d",&n);
return n;
}
	//s0 s1	// a0,	s2,	s3,	s4
void hanoi(int k,int a,int b,int c){//$a not enough? borrow from s
	if(k==1){
		ans++;
		return;
	}
	hanoi(k-1,a,c,b);
	ans++;
	hanoi(k-1,c,b,a);
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
int main(){
	nn=getint();
	ans=0;
	hanoi(nn,1,2,3);
	printf("20373444\n");
	int res = ans;
	printf("%d\n", res);
	printf("%d\n", res + res);
	printf("%d\n", res * res);
	printf("%d\n", res / res);
	printf("%d\n", res - res);
	printf("very weak points\n");
	printf("very weak points\n");
	printf("very weak points\n");
	printf("very weak points\n");
	return 0;
}