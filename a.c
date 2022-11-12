#include <stdio.h>

int getint() {
    int ret;
    scanf("%d", &ret);
    return ret;
}

const int const_val_1[2+1]={1,2,3},const_val_2[2][2]={{4,5},{6,7}};
int var_val_0[8],var_val_1[2]={10,9},var_val_2[2][2]={{3,2},{1,0}};

int func1(int a[],int size_a){
	/* this is Decl test */
	int i=size_a-1;
	while(i>=0){
		a[1]=a[1]+a[i];
		i=i-1;
	}

	printf("a[1]=%d\n",a[1]);
	return a[1];
}
void func2(int a[],int b[][2]){
	/* this is global-local test */
	printf("a[0]=%d,a[1]=%d\n", a[0], a[1]);
	int i=1;
	while(i>=0){
		a[i]=a[i]+var_val_1[i];
		i=i-1;
	}
	printf("a[%d]=%d,a[1]=%d\n", i, a[0], a[1]);
	/*
	int j=1,k=1;
	while(j>=0){
		while(k>=0){
			b[i][j]=b[i][j]+var_val_2[i][j];
			k=k-1;
		}
		j=j-1;
	}*/
	printf("a[0]=%d,a[1]=%d\n", a[0], a[1]);
	b[i][0]=b[i][0]+var_val_2[i][0];
	printf("a[0]=%d,a[1]=%d\n", a[0], a[1]);
	b[i][0]=b[i][1]+var_val_2[i][1];
	printf("a[0]=%d,a[1]=%d\n", a[0], a[1]);
	b[i][0]=b[i][0]+var_val_2[i][0];
	printf("a[0]=%d,a[1]=%d\n", a[0], a[1]);
    b[i][0]=b[i][1]+var_val_2[i][1];
	printf("a[0]=%d,a[1]=%d\n", a[0], a[1]);

}
int main(){
//	freopen("input2.txt","r",stdin);
//	freopen("output2.txt","w",stdout);
	printf("19373650\n");
	int a[2][2]={{0,0},{0,0}};
	int b[2];
	b[0]=100;
	b[1]=50;
	func2(b,a);
	printf("test is over\n");
	return 0;
}