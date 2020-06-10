package com.prepare.prepareurself.quizv2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prepare.prepareurself.quizv2.data.*

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    var quizResponseBodyLiveData : LiveData<QuizResponseModel>?=null
    private var quizRepository:QuizRepository?=null


    init {
        quizRepository = QuizRepository()
        quizResponseBodyLiveData = MutableLiveData()
    }

    fun fetchQuiz(token: String, courseId: Int, level: String) {
        quizResponseBodyLiveData = quizRepository?.fetchQuiz(token,courseId,level)
    }

    fun submitQuiz(token: String,quizId:Int, responses:List<ResponsesModel>):LiveData<QuizAnswerResponse>?{
        return quizRepository?.submitQuiz(token, quizId, responses)
    }

    fun submitIndividualQuiz(token: String,quizId: Int,courseId:Int,questionId:Int, optionId:Int){
        quizRepository?.submitIndividualQuiz(token, quizId, courseId, questionId, optionId)
    }

//    fun createData(){
//
//        val op1 = OptionsModel()
//        op1.optionImage = ""
//        op1._id = "1"
//        op1.option = "This is option 1"
//
//        val op2 = OptionsModel()
//        op2.optionImage = ""
//        op2._id = "2"
//        op2.option = "This is option 1"
//
//        val op3 = OptionsModel()
//        op3.optionImage = ""
//        op3._id = "3"
//        op3.option = "This is option 1"
//
//        val op4 = OptionsModel()
//        op4.optionImage = ""
//        op4._id = "4"
//        op4.option = "This is option 1"
//
//        val options = ArrayList<OptionsModel>()
//        options.add(op1)
//        options.add(op2)
//        options.add(op3)
//        options.add(op4)
//
//        val q1 = QuestionModel()
//        q1._id = "1"
//        q1.questionImage = ""
//        q1.type ="singleCorrect"
//        q1.type = "_id"
//        q1.question =  "This is question 1"
//        q1.options = options
//
//        val q2 = QuestionModel()
//        q2._id = "2"
//        q2.questionImage = ""
//        q2.type ="singleCorrect"
//        q2.type = "_id"
//        q2.question =  "This is question 1"
//        q2.options = options
//
//        val q3 = QuestionModel()
//        q3._id = "3"
//        q3.questionImage = ""
//        q3.type ="singleCorrect"
//        q3.type = "_id"
//        q3.question =  "This is question 1"
//        q3.options = options
//
//        val q4 = QuestionModel()
//        q4._id = "4"
//        q4.questionImage = ""
//        q4.type ="singleCorrect"
//        q4.type = "_id"
//        q4.question =  "This is question 1"
//        q4.options = options
//
//        val q5 = QuestionModel()
//        q5._id = "5"
//        q5.questionImage = ""
//        q5.type ="singleCorrect"
//        q5.type = "_id"
//        q5.question =  "This is question 1"
//        q5.options = options
//
//
//        val instructions = ArrayList<InstructionsModel>()
//        val i1 = InstructionsModel()
//        i1._id = "5ec9fde674d0c0187c5c2198"
//        i1.instruction = "Instruction 1"
//
//        val i2 = InstructionsModel()
//        i2._id = "5ec9fde674d0c0187c5c2198"
//        i2.instruction = "Instruction 1"
//
//        instructions.add(i1)
//        instructions.add(i2)
//
//        val questions = ArrayList<QuestionModel>()
//        questions.add(q1)
//        questions.add(q2)
//        questions.add(q3)
//        questions.add(q4)
//        questions.add(q5)
//
//        val quizResponseBody = QuizResponseBody()
//        quizResponseBody.duration = -1
//        quizResponseBody.questions = questions
//        quizResponseBody.type = "quiz"
//        quizResponseBody.title = "DPP 01"
//        quizResponseBody.description = ""
//        quizResponseBody.instructions = instructions
//
//        quizResponseBodyLiveData.value = quizResponseBody
//
//    }

}