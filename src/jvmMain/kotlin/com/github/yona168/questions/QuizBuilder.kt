package com.github.yona168.questions


/*
class QuizBuilder {
    private val questions = mutableListOf<Question>()

    fun shortAnswer(question: String, answer: String)=questions.add(ShortAnswer(question, answer))
    fun multipleChoice(question:String, buildFunc: MultipleChoiceBuilder.()->Unit){
        val builder=MultipleChoiceBuilder()
        buildFunc(builder)
        if(builder.answer==null){
            throw RuntimeException("No answer supplied to question")
        }
        questions.add(MultipleChoice(question, builder.options, builder.answer!!))
    }
    fun manyChoice(question: String, buildFunc: ManyChoiceBuilder.()->Unit){
        val builder = ManyChoiceBuilder()
        buildFunc(builder)
        if(builder.answers.isEmpty()){
            throw RuntimeException("No answer supplied to question")
        }
        questions.add(ManyChoice(question, builder.options, builder.answers))
    }
    fun build(data: SimpleMeta)=Quiz(data,questions)
    fun build(title: String, author: String)=Quiz(SimpleMeta(title, author), questions)
    inner class MultipleChoiceBuilder{
        internal val options = mutableListOf<String>()
        var answer: Int? = null
        fun option(option: String)=options.add(option)
    }
    inner class ManyChoiceBuilder{
        internal val options = mutableListOf<String>()
        internal val answers = mutableListOf<Int>()
        fun answer(index: Int)=answers.add(index)
        fun option(option: String)=options.add(option)
    }
}
*/
