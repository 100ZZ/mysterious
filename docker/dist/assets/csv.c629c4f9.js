import{d as y,a as i,r as z,E as L,b as U,o as g,c as M,e as r,f as e,w as n,u as d,g as p,s as b,h as P,i as F,j as w,l as S,a6 as $,_ as j}from"./index.7d19d52a.js";import{E as q,a as R,b as G,c as H}from"./push.8fd3fd7d.js";import{E as J,a as f}from"./request.d7d01389.js";import{E as K}from"./el-drawer.8418db40.js";import"./el-overlay.d904f775.js";import{g as O,d as Q,a as W}from"./csv.e5a0f94c.js";import{E as X}from"./index.9aec7bf6.js";const Y={class:"container"},Z={class:"handle-box"},ee=["textContent"],ae={class:"pagination"},te=y({name:"baseCsv"}),le=y({...te,setup(se){const m=i(!1),l=z({srcName:null,testCaseId:null,page:1,size:10}),v=i([]),C=i(0),u=()=>{O(l).then(a=>{if(H(a.data.message),a.data.code!=0)return f.error(a.data.message),!1;v.value=a.data.data.list,C.value=a.data.data.total||10})};u();const D=()=>{l.page=1,u()},B=()=>{l.srcName=null,l.testCaseId=null,u()},x=a=>{l.page=a,u()},V=async a=>{await X.confirm("\u786E\u5B9A\u8981\u5220\u9664\u5417\uFF1F","\u63D0\u793A",{type:"warning"});const t=await Q(a);t.data.code!=0?f.error(t.data.message):(f.success("\u5220\u9664\u6210\u529F"),await u())},E=i(""),k=async a=>{const t=await W(a);E.value=t.data};return(a,t)=>{const _=J,c=L,s=q,I=R,N=K,T=G,h=U("permiss");return g(),M("div",null,[r("div",Y,[r("div",Z,[e(_,{modelValue:l.srcName,"onUpdate:modelValue":t[0]||(t[0]=o=>l.srcName=o),placeholder:"\u6570\u636E\u540D\u79F0",class:"handle-input mr10"},null,8,["modelValue"]),e(_,{modelValue:l.testCaseId,"onUpdate:modelValue":t[1]||(t[1]=o=>l.testCaseId=o),placeholder:"\u7528\u4F8BID",class:"handle-input mr10"},null,8,["modelValue"]),e(c,{type:"primary",icon:d(b),onClick:D},{default:n(()=>[p("\u641C\u7D22")]),_:1},8,["icon"]),e(c,{type:"primary",icon:d(P),onClick:B},{default:n(()=>[p("\u91CD\u7F6E")]),_:1},8,["icon"])]),e(I,{data:v.value,border:"",class:"table",ref:"multipleTable","header-cell-class-name":"table-header"},{default:n(()=>[e(s,{prop:"id",label:"ID",width:"55",align:"center"}),e(s,{prop:"srcName",label:"\u6570\u636E\u540D\u79F0",align:"center"}),e(s,{prop:"description",label:"\u6570\u636E\u63CF\u8FF0",align:"center"}),e(s,{prop:"testCaseId",label:"\u7528\u4F8BID",align:"center"}),e(s,{prop:"creator",label:"\u521B\u5EFA\u4EBA",align:"center"}),e(s,{prop:"createTime",label:"\u521B\u5EFA\u65F6\u95F4",align:"center"}),e(s,{prop:"modifier",label:"\u4FEE\u6539\u4EBA",align:"center"}),e(s,{prop:"modifyTime",label:"\u4FEE\u6539\u65F6\u95F4",align:"center"}),e(s,{label:"\u64CD\u4F5C",width:"120",align:"center"},{default:n(o=>[F((g(),w(c,{style:{"margin-left":"0"},text:"",icon:d(b),class:"bg-blue",onClick:A=>(m.value=!0,k(o.row.id))},{default:n(()=>[p(" \u9884\u89C8 ")]),_:2},1032,["icon","onClick"])),[[h,1]]),F((g(),w(c,{style:{"margin-left":"0"},text:"",icon:d(S),class:"red",onClick:A=>V(o.row.id)},{default:n(()=>[p(" \u5220\u9664 ")]),_:2},1032,["icon","onClick"])),[[h,1]])]),_:1})]),_:1},8,["data"]),e(N,{modelValue:m.value,"onUpdate:modelValue":t[2]||(t[2]=o=>m.value=o),title:"\u6570\u636E\u8BE6\u60C5","show-close":!0,"with-header":!0,size:"60%"},{default:n(()=>[r("pre",null,[r("div",{textContent:$(E.value)},null,8,ee)])]),_:1},8,["modelValue"]),r("div",ae,[e(T,{background:"",layout:"total, prev, pager, next","current-page":l.page,"page-size":l.size,total:C.value,onCurrentChange:x},null,8,["current-page","page-size","total"])])])])}}});const pe=j(le,[["__scopeId","data-v-e677d846"]]);export{pe as default};
