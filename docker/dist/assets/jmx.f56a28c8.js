import{d as w,a as i,r as J,E as j,b as z,o as g,c as L,e as r,f as e,w as s,u as d,g as p,s as E,h as U,i as x,j as F,l as M,a6 as P,_ as S}from"./index.7d19d52a.js";import{E as $,a as q,b as R,c as G}from"./push.8fd3fd7d.js";import{E as H,a as f}from"./request.d7d01389.js";import{E as K}from"./el-drawer.8418db40.js";import"./el-overlay.d904f775.js";import{e as O,d as Q,a as W}from"./jmx.b6ce8221.js";import{E as X}from"./index.9aec7bf6.js";const Y={class:"container"},Z={class:"handle-box"},ee=["textContent"],ae={class:"pagination"},te=w({name:"baseJmx"}),le=w({...te,setup(oe){const m=i(!1),l=J({srcName:null,testCaseId:null,page:1,size:10}),C=i([]),b=i(0),u=()=>{O(l).then(a=>{if(G(a.data.message),a.data.code!=0)return f.error(a.data.message),!1;C.value=a.data.data.list,b.value=a.data.data.total||10})};u();const y=()=>{l.page=1,u()},D=()=>{l.srcName=null,l.testCaseId=null,u()},B=a=>{l.page=a,u()},V=async a=>{await X.confirm("\u786E\u5B9A\u8981\u5220\u9664\u5417\uFF1F","\u63D0\u793A",{type:"warning"});const t=await Q(a);t.data.code!=0?f.error(t.data.message):(f.success("\u5220\u9664\u6210\u529F"),await u())},h=i(""),k=async a=>{const t=await W(a);h.value=t.data};return(a,t)=>{const _=H,c=j,o=$,A=q,I=K,N=R,v=z("permiss");return g(),L("div",null,[r("div",Y,[r("div",Z,[e(_,{modelValue:l.srcName,"onUpdate:modelValue":t[0]||(t[0]=n=>l.srcName=n),placeholder:"\u811A\u672C\u540D\u79F0",class:"handle-input mr10"},null,8,["modelValue"]),e(_,{modelValue:l.testCaseId,"onUpdate:modelValue":t[1]||(t[1]=n=>l.testCaseId=n),placeholder:"\u7528\u4F8BID",class:"handle-input mr10"},null,8,["modelValue"]),e(c,{type:"primary",icon:d(E),onClick:y},{default:s(()=>[p("\u641C\u7D22")]),_:1},8,["icon"]),e(c,{type:"primary",icon:d(U),onClick:D},{default:s(()=>[p("\u91CD\u7F6E")]),_:1},8,["icon"])]),e(A,{data:C.value,border:"",class:"table",ref:"multipleTable","header-cell-class-name":"table-header"},{default:s(()=>[e(o,{prop:"id",label:"ID",width:"55",align:"center"}),e(o,{prop:"srcName",label:"\u811A\u672C\u540D\u79F0",align:"center"}),e(o,{prop:"description",label:"\u811A\u672C\u63CF\u8FF0",align:"center"}),e(o,{prop:"testCaseId",label:"\u7528\u4F8BID",align:"center"}),e(o,{prop:"creator",label:"\u521B\u5EFA\u4EBA",align:"center"}),e(o,{prop:"createTime",label:"\u521B\u5EFA\u65F6\u95F4",align:"center"}),e(o,{prop:"modifier",label:"\u4FEE\u6539\u4EBA",align:"center"}),e(o,{prop:"modifyTime",label:"\u4FEE\u6539\u65F6\u95F4",align:"center"}),e(o,{label:"\u64CD\u4F5C",width:"120",align:"center"},{default:s(n=>[x((g(),F(c,{style:{"margin-left":"0"},text:"",icon:d(E),class:"bg-blue",onClick:T=>(m.value=!0,k(n.row.id))},{default:s(()=>[p(" \u9884\u89C8 ")]),_:2},1032,["icon","onClick"])),[[v,1]]),x((g(),F(c,{style:{"margin-left":"0"},text:"",icon:d(M),class:"red",onClick:T=>V(n.row.id)},{default:s(()=>[p(" \u5220\u9664 ")]),_:2},1032,["icon","onClick"])),[[v,1]])]),_:1})]),_:1},8,["data"]),e(I,{modelValue:m.value,"onUpdate:modelValue":t[2]||(t[2]=n=>m.value=n),title:"\u811A\u672C\u8BE6\u60C5","show-close":!0,"with-header":!0,size:"60%"},{default:s(()=>[r("pre",null,[r("div",{textContent:P(h.value)},null,8,ee)])]),_:1},8,["modelValue"]),r("div",ae,[e(N,{background:"",layout:"total, prev, pager, next","current-page":l.page,"page-size":l.size,total:b.value,onCurrentChange:B},null,8,["current-page","page-size","total"])])])])}}});const pe=S(le,[["__scopeId","data-v-6a8414b4"]]);export{pe as default};
