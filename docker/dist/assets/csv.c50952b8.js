import{d as k,a as _,r as L,E as U,b as j,o as g,c as M,e as u,f as s,w as o,u as i,g as m,s as h,h as P,a7 as f,i as D,j as B,l as S,_ as q}from"./index.e83529d4.js";import{E as R,a as G,b as H,c as J,j as K}from"./push.f24fb052.js";import{E as O}from"./el-drawer.62edb61d.js";import"./el-overlay.02e632ca.js";import{E as Q,a as d}from"./request.d3b43c42.js";import{g as W,d as X,a as Y,v as Z}from"./csv.c6ff57bc.js";import{E as ee}from"./index.6313ba76.js";const ae={class:"container"},te={class:"handle-box"},se=["onClick"],le=["onClick"],oe=["textContent"],ne={class:"pagination"},ue=k({name:"baseCsv"}),re=k({...ue,setup(ie){const C=_(!1),l=L({srcName:null,testCaseId:null,page:1,size:10}),w=_([]),E=_(0),r=()=>{W(l).then(t=>{if(J(t.data.message),t.data.code!=0)return d.error(t.data.message),!1;w.value=t.data.data.list,E.value=t.data.data.total||10})};r();const y=()=>{l.page=1,r()},x=()=>{l.srcName=null,l.testCaseId=null,r()},V=t=>{l.page=t,r()},I=async t=>{await ee.confirm("\u786E\u5B9A\u8981\u5220\u9664\u5417\uFF1F","\u63D0\u793A",{type:"warning"});const e=await X(t);e.data.code!=0?d.error(e.data.message):(d.success("\u5220\u9664\u6210\u529F"),await r())},N=async(t,e)=>{if(!e){d.error("csv\u6570\u636E\u6587\u4EF6\u4E0D\u5B58\u5728");return}(await Y(t,e)).success||d.error("\u4E0B\u8F7D\u5931\u8D25, \u8BF7\u91CD\u8BD5")},b=_(""),T=async t=>{const e=await Z(t);b.value=e.data};return(t,e)=>{const c=Q,p=U,n=R,z=G,A=O,$=H,F=j("permiss");return g(),M("div",null,[u("div",ae,[u("div",te,[s(c,{modelValue:l.srcName,"onUpdate:modelValue":e[0]||(e[0]=a=>l.srcName=a),placeholder:"\u6570\u636E\u540D\u79F0",class:"handle-input mr10"},null,8,["modelValue"]),s(c,{modelValue:l.testCaseId,"onUpdate:modelValue":e[1]||(e[1]=a=>l.testCaseId=a),placeholder:"\u7528\u4F8B",class:"handle-input mr10"},null,8,["modelValue"]),s(p,{type:"primary",icon:i(h),onClick:y},{default:o(()=>e[3]||(e[3]=[m("\u641C\u7D22")])),_:1},8,["icon"]),s(p,{type:"primary",icon:i(P),onClick:x},{default:o(()=>e[4]||(e[4]=[m("\u91CD\u7F6E")])),_:1},8,["icon"])]),s(z,{data:w.value,border:"",class:"table",ref:"multipleTable","header-cell-class-name":"table-header"},{default:o(()=>[s(n,{prop:"id",label:"\u7F16\u53F7",width:"55",align:"center"}),s(n,{prop:"srcName",label:"\u540D\u79F0",align:"center"},{default:o(a=>[u("div",{onClick:v=>N(a.row.id,a.row.dstName),style:{color:"blue",cursor:"pointer"}},f(a.row.dstName),9,se)]),_:1}),s(n,{prop:"description",label:"\u63CF\u8FF0",align:"center"}),s(n,{prop:"testCaseId",label:"\u7528\u4F8B",align:"center"},{default:o(a=>[u("span",{onClick:v=>i(K)(a.row.testCaseId),style:{cursor:"pointer",color:"blue"}},f(a.row.testCaseId),9,le)]),_:1}),s(n,{prop:"creator",label:"\u521B\u5EFA\u4EBA",align:"center"}),s(n,{prop:"createTime",label:"\u521B\u5EFA\u65F6\u95F4",align:"center"}),s(n,{label:"\u64CD\u4F5C",width:"200",align:"center"},{default:o(a=>[D((g(),B(p,{text:"",icon:i(h),class:"bg-blue",onClick:v=>(C.value=!0,T(a.row.id))},{default:o(()=>e[5]||(e[5]=[m(" \u9884\u89C8 ")])),_:2},1032,["icon","onClick"])),[[F,1]]),D((g(),B(p,{text:"",icon:i(S),class:"red",onClick:v=>I(a.row.id)},{default:o(()=>e[6]||(e[6]=[m(" \u5220\u9664 ")])),_:2},1032,["icon","onClick"])),[[F,1]])]),_:1})]),_:1},8,["data"]),s(A,{modelValue:C.value,"onUpdate:modelValue":e[2]||(e[2]=a=>C.value=a),title:"\u6570\u636E\u8BE6\u60C5","show-close":!0,"with-header":!0,size:"60%"},{default:o(()=>[u("pre",null,[u("div",{textContent:f(b.value)},null,8,oe)])]),_:1},8,["modelValue"]),u("div",ne,[s($,{background:"",layout:"total, prev, pager, next","current-page":l.page,"page-size":l.size,total:E.value,onCurrentChange:V},null,8,["current-page","page-size","total"])])])])}}});const ge=q(re,[["__scopeId","data-v-551ee549"]]);export{ge as default};
