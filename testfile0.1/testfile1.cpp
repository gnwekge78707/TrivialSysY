#include <stdio.h>

int fac[10000];

const int constArrDef[2][2]={{0,0},{0,0}};


int fact(int n) {
	fac[0]=1;
	int nfac,ncur;
	nfac=1;
	ncur=0;
	int i=1;
	while(i<n+1){
		int temp; //s4
		temp=fac[0];
		fac[0]=0;
		int j=0;
		while(j<nfac){
			fac[j]+=i*temp;
			temp=fac[j+1];
			fac[j+1]=fac[j]/10;
			fac[j]%=10;
			j++;
		}
		while(fac[nfac]>0){
			fac[nfac+1]=fac[nfac]/10;
			fac[nfac]%=10;
			nfac++;
		}
		i++;
	}
	return fac[0];
}

int a[10001],nn;
	int qsort(int l1,int r1){
		int i,l,r,m,tmp;
		l=l1;r=r1;
		m=(a[l]+a[r])/2;//                  ...l1.........r1...
		while(l<=r){                  //        1 2 4 5 7 8        m=4.5
			while(a[l]<m) l++;//                    l r    
			while(a[r]>m) r--;
			if(l<=r){
				tmp=a[l];a[l]=a[r];a[r]=tmp;
				l++;r--;
			}
		}
		if(l1<r) qsort(l1,r);
		if(l<r1) qsort(l,r1);
	}

int satisfyReqSub(int ad1[], int ad2[][2], int ck) {
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

int getint(){
int n;
scanf("%d",&n);
return n;
}

int main() {
	int asdf;
	nn = getint();
	int i,j,k;
	i=1;
	while(i<=nn) {
		a[i]=getint();
		i++;
	}
	--i;
	i--;
	qsort(1,nn);
	printf("20373444\n");
	printf("very weak points\n");
	printf("%d\n", a[1]);
	printf("%d\n", fact(13));
	satisfyReq();
	int res = fact(6);
	printf("%d\n", res / res -10 + res * (res / 2 + (res+1)%122)/ (res / res + 2) + (+(-(+(-res))))*(res % ( res - res + 2*(res/res))));
	printf("very weak points\n");
	printf("very weak points\n");
	printf("very weak points\n");
	printf("very weak points\n");
	printf("very weak points\n");
	return 0;
}