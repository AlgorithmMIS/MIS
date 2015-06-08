#include<stdio.h>
#include<stdlib.h>
#include<errno.h>
#include<time.h>
typedef struct c_neib{
	int degree;
	int t_size;
	int *array;
}co_neibor;
typedef struct pair{
	int x,y;
}_pair;
int v_n;
int e_n;
#define VERTEXNUM 4000
#define FINAL_STEP 200000
int step=0;
int tabuAdd=-1;
int taburemove_vetex=-1;
int uinc_n=VERTEXNUM;
int inc_n=0;
int ub;
int list_size=0;
typedef struct ed_list{
	int x,y;
	struct ed_list *next;
}edges;
co_neibor c_neib[VERTEXNUM];
int **e_w;
int inc[VERTEXNUM]={0};
int outofc[VERTEXNUM];
int dscore[VERTEXNUM]={0};
FILE *file;
edges *LIST=NULL,*lnext=NULL;
void readfile();
void initial();
int compute_score(int inc,int outc);
_pair chooseSwapPair();
_pair randomSwapPair();
void removeDelta();
void add(int v);
void remove_vetex(int v);
void readfile()
{
	int i,j,tpa,tpb,oldsize;
	char a[3],b[5];
	e_w=(int **)malloc(sizeof(int*)*VERTEXNUM);
	for(i=0;i<VERTEXNUM;i++)
	{
		e_w[i]=(int *)malloc(sizeof(int)*VERTEXNUM);
		for(j=0;j<VERTEXNUM;j++)
			e_w[i][j]=0;
	}
	for(i=0;i<VERTEXNUM;i++)
	{
		c_neib[i].degree=0;
		c_neib[i].array=(int *)malloc(sizeof(int)*VERTEXNUM);
	}
	if((file=fopen("frb.mis","r"))==NULL)
		printf("wrong\n");
	fscanf(file,"%s%s%d%d",a,b,&v_n,&e_n);
	for(i=0;i<e_n;i++)
	{
		fscanf(file,"%s%d%d",a,&tpa,&tpb);
		tpa--;tpb--;
		c_neib[tpa].array[c_neib[tpa].degree++]=tpb;
		c_neib[tpb].array[c_neib[tpb].degree++]=tpa;
	}
}
void initial()
{
	int i=0,j=0;
	int w[VERTEXNUM]={0};
	int remain[VERTEXNUM];
	int remain_size=0;
	int uncov=e_n;
	edges *tmp;
	for(i=0;i<v_n;i++)
	{
		w[i]=c_neib[i].degree;
		for(;j<w[i];j++)
			e_w[i][c_neib[i].array[j]]=1;
	}
	while(uncov>0)
	{
		int max=-1;
		int maxv=-1;
		for(i=0;i<v_n;i++)
		{
			if(inc[i])
				continue;
			if(w[i]>max){
				max=w[i];
				maxv=i;
			}

		}
		inc[maxv]=1;
		inc_n++;
		uinc_n--;
		ub++;
		uncov-=max;
		for(j=0;j<c_neib[maxv].degree;j++){
			w[c_neib[maxv].array[j]]--;
		}	
	}
	for(i=0;i<v_n;i++)
	{
		for(j=0;j<c_neib[i].degree;j++)
		{
			if(!inc[c_neib[i].array[j]])
				dscore[i]++;
		}
	}
	for(i=0;i<v_n;i++)
		if(!inc[i]){
			remain[remain_size++]=i;
		}
	uinc_n=remain_size;
	inc_n=VERTEXNUM-remain_size;
	for(i=0;i<remain_size;i++)
		for(j=0;j<remain_size;j++)
		{
			if(e_w[i][j]>0)
			{
				tmp=(edges*)malloc(sizeof(edges));
				tmp->x=i>j?j:i;
				tmp->y=i>j?i:j;
				tmp->next=NULL;
				if(LIST==NULL)
				{
					LIST=tmp;
					lnext=LIST;
				}
				else
				{
					lnext->next=tmp;
					lnext=lnext->next;
				}
				list_size++;
			}
		}
}
int compute_score(int inc,int outc)
{
	return dscore[outc]-dscore[inc]+e_w[inc][outc];
}
_pair chooseSwapPair()
{
	srand(time(NULL));
	edges *tmp;
	_pair result;
	int r,j,u;
	for(tmp=LIST;tmp!=NULL;tmp=tmp->next)
	{
		r=rand()%v_n;
		for(j=0;j<v_n;j++)
		{
			u=(j+r)%v_n;
			if(!inc[u])
				continue;
			if(u!=taburemove_vetex&&tmp->x!=tabuAdd)
			{
				if(compute_score(u,tmp->x)>0){
				result.x=u;
				result.y=tmp->x;
				return result;}
			} 
			if(u!=taburemove_vetex&&tmp->y!=tabuAdd)
			{
				if(compute_score(u,tmp->y)>0){
				result.x=u;
				result.y=tmp->y;
				return result;}
			}
		}
	}
	result.x=-1;
	result.y=-1;
	return result;
}
_pair randomSwapPair()
{
	_pair result;
	int x,i;
	int index;
	edges *tmp=LIST;
	srand(time(NULL));
	do
	x=rand()%v_n;
	while(!inc[x]);
	index=rand()%list_size;
	for(i=0;i<index;i++)
	{
		tmp=tmp->next;
	}
	if(rand()%2)
	{
		result.x=x;
		result.y=tmp->x;
		return result;
	}
	result.x=x;
	result.y=tmp->y;
	return result;
}
void removeDelta()
{
	int max=-9999999;
	int maxv=-1;
	int i;
	for(i=0;i<v_n;i++)
	{
		if(inc[i]&&dscore[i]>max)
		{
			max=dscore[i];
			maxv=i;
		}
	}
	remove_vetex(maxv);
}
void add(int v)
{
	edges *tmp;
	inc[v]=1;
	int i=0,j=0;
	inc_n++;
	uinc_n--;
	for(lnext=LIST;lnext!=NULL;)
	{
		if(lnext->x==v||lnext->y==v)
		{
			list_size--;
			if(lnext==LIST)
			{
				tmp=LIST;
				LIST=LIST->next;
				lnext=LIST;
				free(tmp);
				tmp=LIST;
			}
			else
			{
				tmp->next=lnext->next;
				free(lnext);
				lnext=tmp->next;
			}
			if(!list_size)
				break;
		}
		else
		{
			tmp=lnext;
			lnext=lnext->next;
		}
	}
	for(;j<c_neib[v].degree;j++)//update dscore
	{
		dscore[c_neib[v].array[j]]-=e_w[v][c_neib[v].array[j]];
	}
}
void remove_vetex(int v)
{
	int i=0,neighbor;
	edges *tmp;
	inc[v]=0;
	uinc_n++;
	inc_n--;
	for(;i<c_neib[v].degree;i++)
	{
		neighbor=c_neib[v].array[i];
		if(!inc[neighbor])
		{
			tmp=(edges*)malloc(sizeof(edges));
			tmp->x=v>neighbor?neighbor:v;
			tmp->y=v>neighbor?v:neighbor;
			tmp->next=LIST;
			list_size++;
			LIST=tmp;
		}
		dscore[neighbor]+=e_w[v][neighbor];
	}
}
int main()
{
	int i;
	_pair swapair;
	readfile();
	initial();
	printf("after initial,ud=%d,uinc_n=%d\n",ub,uinc_n);
	removeDelta();
	while(step++<FINAL_STEP)
	{
		if(list_size!=0)
		{
			swapair=chooseSwapPair();
			if(swapair.x==-1){
				for(lnext=LIST;lnext!=NULL;lnext=lnext->next)
				{
					e_w[lnext->x][lnext->y]++;
					e_w[lnext->y][lnext->x]++;
					if(!inc[lnext->x])
						dscore[lnext->y]++;
					if(!inc[lnext->y])
						dscore[lnext->x]++;
				}
				swapair=randomSwapPair();
			}
			remove_vetex(swapair.x);
			add(swapair.y);
			tabuAdd=swapair.x;
			taburemove_vetex=swapair.y;
		}
		if(inc_n+list_size<ub)
		{
			ub=inc_n+list_size;
			if(list_size==0)
				printf("uinc_n size:%d\n",uinc_n);
			removeDelta();
		}
	}
	//printf("uinc_n:%d\n",uinc_n);
	return 0;
}
