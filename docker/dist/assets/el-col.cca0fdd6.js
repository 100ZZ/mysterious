import{x as g,d as f,H as _,D as o,K as O,o as $,j as h,w,N as v,O as j,u as c,a6 as N,a5 as x,P as C,aa as R,z as u,bn as r,R as S,aS as b,aX as K}from"./index.392261ad.js";const E=Symbol("rowContextKey"),P=["start","center","end","space-around","space-between","space-evenly"],k=["top","middle","bottom"],B=g({tag:{type:String,default:"div"},gutter:{type:Number,default:0},justify:{type:String,values:P,default:"start"},align:{type:String,values:k}}),D=f({name:"ElRow"}),L=f({...D,props:B,setup(p){const e=p,l=_("row"),a=o(()=>e.gutter);O(E,{gutter:a});const i=o(()=>{const t={};return e.gutter&&(t.marginRight=t.marginLeft=`-${e.gutter/2}px`),t}),d=o(()=>[l.b(),l.is(`justify-${e.justify}`,e.justify!=="start"),l.is(`align-${e.align}`,!!e.align)]);return(t,m)=>($(),h(x(t.tag),{class:j(c(d)),style:N(c(i))},{default:w(()=>[v(t.$slots,"default")]),_:3},8,["class","style"]))}});var A=C(L,[["__file","row.vue"]]);const F=R(A),H=g({tag:{type:String,default:"div"},span:{type:Number,default:24},offset:{type:Number,default:0},pull:{type:Number,default:0},push:{type:Number,default:0},xs:{type:u([Number,Object]),default:()=>r({})},sm:{type:u([Number,Object]),default:()=>r({})},md:{type:u([Number,Object]),default:()=>r({})},lg:{type:u([Number,Object]),default:()=>r({})},xl:{type:u([Number,Object]),default:()=>r({})}}),I=f({name:"ElCol"}),J=f({...I,props:H,setup(p){const e=p,{gutter:l}=S(E,{gutter:o(()=>0)}),a=_("col"),i=o(()=>{const t={};return l.value&&(t.paddingLeft=t.paddingRight=`${l.value/2}px`),t}),d=o(()=>{const t=[];return["span","offset","pull","push"].forEach(s=>{const n=e[s];b(n)&&(s==="span"?t.push(a.b(`${e[s]}`)):n>0&&t.push(a.b(`${s}-${e[s]}`)))}),["xs","sm","md","lg","xl"].forEach(s=>{b(e[s])?t.push(a.b(`${s}-${e[s]}`)):K(e[s])&&Object.entries(e[s]).forEach(([n,y])=>{t.push(n!=="span"?a.b(`${s}-${n}-${y}`):a.b(`${s}-${y}`))})}),l.value&&t.push(a.is("guttered")),[a.b(),t]});return(t,m)=>($(),h(x(t.tag),{class:j(c(d)),style:N(c(i))},{default:w(()=>[v(t.$slots,"default")]),_:3},8,["class","style"]))}});var T=C(J,[["__file","col.vue"]]);const G=R(T);export{G as E,F as a};
