import{d as I,a as B,ck as P,r as S,E as G,b as H,o as u,c as K,e as _,f as a,w as t,u as p,g as s,s as R,h as Q,a7 as L,j as i,a8 as f,i as D,cq as W,cr as X,l as Y,_ as Z}from"./index.392261ad.js";import{E as ee}from"./el-drawer.0fda271c.js";import"./el-overlay.e3f39179.js";import{E as te,e as ae,a as oe,b as le,c as ne,j as se}from"./push.3f335b6f.js";import{E as re,a as ue}from"./el-col.cca0fdd6.js";import{s as b,E as de,a as y}from"./request.7281df1d.js";import{E as ie}from"./index.198fd07b.js";const ce=r=>b({url:"/report/listByTestCase",method:"get",params:r}),pe=async r=>{try{const c=await b({url:"/report/download/"+r,method:"get",responseType:"blob"}),w=window.URL.createObjectURL(new Blob([c.data])),l=document.createElement("a");return l.href=w,l.setAttribute("download",r+"_data.zip"),document.body.appendChild(l),l.click(),window.URL.revokeObjectURL(w),{success:!0}}catch(c){return console.error("Error downloading file:",c),{success:!1,error:c}}},me=r=>b({url:"/report/getJMeterLog/"+r,method:"get"}),ge=r=>b({url:"/report/clean/"+r,method:"get"}),_e=r=>b({url:"/report/view/"+r,method:"get"}),fe={class:"container"},we={class:"handle-box"},Ce=["onClick"],ye={class:"pagination"},be=["textContent"],ve=I({name:"baseJmx"}),Ee=I({...ve,setup(r){const c=B(!1),w=P(),l=S({name:w.query.name||null,testCaseId:w.query.testCaseId||null,page:1,size:10}),x=B([]),h=B(0),C=()=>{ce(l).then(n=>{if(ne(n.data.message),n.data.code!=0)return y.error(n.data.message),!1;x.value=n.data.data.list,h.value=n.data.data.total||10})};C();const A=()=>{l.page=1,C()},j=()=>{l.name=null,l.testCaseId=null,C()},U=n=>{l.page=n,C()},z=async n=>{await ie.confirm("\u786E\u5B9A\u8981\u5220\u9664\u5417\uFF1F","\u63D0\u793A",{type:"warning"});const e=await ge(n);e.data.code!=0?y.error(e.data.message):(y.success("\u5220\u9664\u6210\u529F"),await C())},$=async n=>{(await pe(n)).success||y.error("\u4E0B\u8F7D\u5931\u8D25, \u8BF7\u91CD\u8BD5")},q=async n=>{const e=await _e(n);e.data.code!=0?y.error(e.data.message):window.open(e.data.data,"_blank")},V=B(""),M=async n=>{const e=await me(n);console.log("res: ",e),V.value=e.data};return(n,e)=>{const v=de,m=G,d=te,g=ae,E=re,T=ue,N=oe,J=le,O=ee,k=H("permiss");return u(),K("div",null,[_("div",fe,[_("div",we,[a(v,{modelValue:l.name,"onUpdate:modelValue":e[0]||(e[0]=o=>l.name=o),placeholder:"\u62A5\u544A\u540D\u79F0",class:"handle-input mr10"},null,8,["modelValue"]),a(v,{modelValue:l.testCaseId,"onUpdate:modelValue":e[1]||(e[1]=o=>l.testCaseId=o),placeholder:"\u7528\u4F8B",class:"handle-input mr10"},null,8,["modelValue"]),a(m,{type:"primary",icon:p(R),onClick:A},{default:t(()=>e[3]||(e[3]=[s("\u641C\u7D22")])),_:1},8,["icon"]),a(m,{type:"primary",icon:p(Q),onClick:j},{default:t(()=>e[4]||(e[4]=[s("\u91CD\u7F6E")])),_:1},8,["icon"])]),a(N,{data:x.value,border:"",class:"table",ref:"multipleTable","header-cell-class-name":"table-header"},{default:t(()=>[a(d,{prop:"id",label:"\u7F16\u53F7",width:"55",align:"center"}),a(d,{prop:"name",label:"\u540D\u79F0",align:"center"}),a(d,{prop:"description",label:"\u63CF\u8FF0",align:"center"}),a(d,{prop:"testCaseId",label:"\u7528\u4F8B",align:"center"},{default:t(o=>[_("span",{onClick:F=>p(se)(o.row.testCaseId),style:{cursor:"pointer",color:"blue"}},L(o.row.testCaseId),9,Ce)]),_:1}),a(d,{prop:"execType",label:"\u7C7B\u578B",align:"center"},{default:t(o=>[o.row.execType===1?(u(),i(g,{key:0,type:"primary"},{default:t(()=>e[5]||(e[5]=[s("\u8C03\u8BD5")])),_:1})):f("",!0),o.row.execType===2?(u(),i(g,{key:1,type:"danger"},{default:t(()=>e[6]||(e[6]=[s("\u538B\u6D4B")])),_:1})):f("",!0)]),_:1}),a(d,{prop:"status",label:"\u72B6\u6001",align:"center"},{default:t(o=>[o.row.status===0?(u(),i(g,{key:0,type:"info"},{default:t(()=>e[7]||(e[7]=[s("\u6CA1\u6709\u6267\u884C")])),_:1})):f("",!0),o.row.status===1?(u(),i(g,{key:1,type:"warning"},{default:t(()=>e[8]||(e[8]=[s("\u6B63\u5728\u6267\u884C")])),_:1})):f("",!0),o.row.status===2?(u(),i(g,{key:2,type:"success"},{default:t(()=>e[9]||(e[9]=[s("\u6267\u884C\u6210\u529F")])),_:1})):f("",!0),o.row.status===3?(u(),i(g,{key:3,type:"danger"},{default:t(()=>e[10]||(e[10]=[s("\u6267\u884C\u5F02\u5E38")])),_:1})):f("",!0)]),_:1}),a(d,{prop:"responseData",label:"\u7ED3\u679C",align:"center"}),a(d,{prop:"creator",label:"\u521B\u5EFA\u4EBA",align:"center"}),a(d,{prop:"createTime",label:"\u521B\u5EFA\u65F6\u95F4",align:"center"}),a(d,{label:"\u64CD\u4F5C",width:"200",align:"center"},{default:t(o=>[a(T,{type:"flex",justify:"center"},{default:t(()=>[a(E,{span:12},{default:t(()=>[D((u(),i(m,{style:{"margin-left":"0"},text:"",icon:p(W),class:"bg-blue",onClick:F=>q(o.row.id)},{default:t(()=>e[11]||(e[11]=[s(" \u9884\u89C8 ")])),_:2},1032,["icon","onClick"])),[[k,1]])]),_:2},1024),a(E,{span:12},{default:t(()=>[D((u(),i(m,{style:{"margin-left":"0"},text:"",icon:p(X),class:"bg-blue",onClick:F=>$(o.row.id)},{default:t(()=>e[12]||(e[12]=[s(" \u4E0B\u8F7D ")])),_:2},1032,["icon","onClick"])),[[k,1]])]),_:2},1024)]),_:2},1024),a(T,{type:"flex",justify:"center"},{default:t(()=>[a(E,{span:12},{default:t(()=>[D((u(),i(m,{style:{"margin-left":"0"},text:"",icon:p(R),class:"bg-blue",onClick:F=>(c.value=!0,M(o.row.id))},{default:t(()=>e[13]||(e[13]=[s(" \u65E5\u5FD7 ")])),_:2},1032,["icon","onClick"])),[[k,1]])]),_:2},1024),a(E,{span:12},{default:t(()=>[D((u(),i(m,{style:{"margin-left":"0"},text:"",icon:p(Y),class:"red",onClick:F=>z(o.row.id)},{default:t(()=>e[14]||(e[14]=[s(" \u5220\u9664 ")])),_:2},1032,["icon","onClick"])),[[k,1]])]),_:2},1024)]),_:2},1024)]),_:1})]),_:1},8,["data"]),_("div",ye,[a(J,{background:"",layout:"total, prev, pager, next","current-page":l.page,"page-size":l.size,total:h.value,onCurrentChange:U},null,8,["current-page","page-size","total"])])]),a(O,{modelValue:c.value,"onUpdate:modelValue":e[2]||(e[2]=o=>c.value=o),title:"jmeter.log\u65E5\u5FD7","show-close":!0,"with-header":!0,size:"60%"},{default:t(()=>[_("pre",null,[_("div",{textContent:L(V.value)},null,8,be)])]),_:1},8,["modelValue"])])}}});const Te=Z(Ee,[["__scopeId","data-v-ea341c85"]]);export{Te as default};
