package com.example.shopick.datamodels

class MyPreferencesModel {
    var question:String?=null
    var ans:String?=null

    constructor(){}

    constructor(question:String?, ans:String?){
        this.question=question
        this.ans=ans
    }
}