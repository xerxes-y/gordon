print('Start #################################################################');
db.createUser({
    user: 'root',
    pwd: 'root',
    roles: [
        {
            role: 'readWrite',
            db: 'kitchen',
        }
    ]
});
db = new Mongo().getDB("kitchen");
db.createCollection('recipe');
db.createCollection('user');
db.user.insertMany([
    {
        active: true,
        email: "user@gordon-ramsay-kitchen.com",
        password: "{bcrypt}$2a$10$WcZjjyMLVxSEOI/GE16bUOdvBkRZ/6NSki.1TTnZQ.3wfNSrqoe4S",
        roles: ["ROLE_USER"],
        username: "user"
    },
    {
        active: true,
        email: "admin@gordon-ramsay-kitchen.com",
        password: "{bcrypt}$2a$10$wtPjOLGS4Eq10kihYjzSYOOmzG5zBLMkz2zk7alu14NJLROB.UK/i",
        roles: ["ROLE_USER", "ROLE_ADMIN"],
        username: "admin"
    }
])
db.recipe.insertMany([
    {
        ingredients: [{name:"cumin seeds", amount:"2 tsp" }, {name:"chilli flakes", amount:"pinch"}, {name:"olive oil", amount:"2 tbsp"}, {name:"carrots", amount:"600g"}, {name:"milk", amount:"125"}],
        instruction: "STEP 1 Heat a large saucepan and dry-fry 2 tsp cumin seeds and a pinch of chilli flakes for 1 min, or until they start to jump around the pan and release their aromas. STEP 2 Scoop out about half with a spoon and set aside. Add 2 tbsp olive oil, 600g coarsely grated carrots, 140g split red lentils, 1l hot vegetable stock and 125ml milk to the pan and bring to the boil. STEP 3 Simmer for 15 mins until the lentils have swollen and softened. STEP 4 Whizz the soup with a stick blender or in a food processor until smooth (or leave it chunky if you prefer). STEP 5 Season to taste and finish with a dollop of plain yogurt and a sprinkling of the reserved toasted spices. Serve with warmed naan breads.",
        name: "Spiced carrot & lentil soup",
        serves: 4,
        type: "NON_VEGETARIAN"
    },
    {
        ingredients: [{name:"aubergine", amount:1}, {name:"red onion", amount:"1"}, {name:"carrots", amount:"2"}, {name:"puy lentils", amount:"70g"}, {name:"red lentils", amount:"30g"}, {name:"kidney beans", amount:"400g"}, {name:"dark soy sauce", amount:"3tbsp"}, {name:"chopped tomatoes", amount:"400g"}, {name:"dark chocolate", amount:"20g"}, {name:"chilli powder", amount:"1/4 tsp"}, {name:"dried oregano", amount:"2 tsp"}, {name:"ground cumin", amount :"2 tsp"}, {name:"sweet smoked paprika", amount:"2 tsp"}, {name:"coriander", amount:"1 tsp"}, {name:"cinnamon", amount:"1 tsp"}, {name:"vegetable stock", amount:"800ml"}, {name:"lime", amount:"1/2"}],
        instruction: "STEP 1 If you have a gas hob, put the aubergine directly onto a lit ring to char completely, turning occasionally with kitchen tongs, until burnt all over. Alternatively, use a barbecue or heat the grill to its highest setting and cook, turning occasionally, until completely blackened (the grill won’t give you the same smoky flavour). Set aside to cool on a plate, then peel off the charred skin and remove the stem. Roughly chop the flesh and set aside.STEP 2 In a large pan, heat the oil, add the onion and carrots with a pinch of salt, and fry over a low-medium heat for 15-20 mins until the carrots have softened.STEP 3 Add the aubergine, both types of lentils, the kidney beans with the liquid from the can, soy sauce, tomatoes, chocolate, chilli powder, oregano and the spices. Stir to combine, then pour in the stock. Bring to the boil, then turn down the heat to very low. Cover with a lid and cook for 1½ hrs, checking and stirring every 15-20 mins to prevent it from burning.STEP 4 Remove the lid and let the mixture simmer over a low-medium heat, stirring occasionally, for about 15 mins until you get a thick sauce. Stir in the lime juice and taste for seasoning – add more salt if needed. Serve hot over rice with whichever accompaniments you want!",
        name: "Burnt aubergine veggie chilli",
        serves: 4,
        type: "VEGETARIAN"
    },
    {
        ingredients: [{name:"potato", amount:"400g"}, {name:"olive oil", amount:"2 tbsp"}, {name:"asparagus", amount:"8"}, {name:"handfuls cherry tomatoes", amount:"2"}, {name:"handfuls cherry tomatoes", amount:"2"}, {name:"salmon fillets", amount:"140g"}],
        instruction: "STEP 1 Heat oven to 220C/fan 200C/gas 7. Tip the potatoes and 1 tbsp of olive oil into an ovenproof dish, then roast the potatoes for 20 mins until starting to brown. Toss the asparagus in with the potatoes, then return to the oven for 15 mins. STEP 2 Throw in the cherry tomatoes and vinegar and nestle the salmon amongst the vegetables. Drizzle with the remaining oil and return to the oven for a final 10-15 mins until the salmon is cooked. Scatter over the basil leaves and serve everything scooped straight from the dish.",
        name: "One-pan salmon with roast asparagus",
        serves: 2,
        type: "NON_VEGETARIAN"
    },
    {
        ingredients: [{name:"golden caster sugar", amount:"200g"}, {name:"cocoa powder", amount:"4 tbsp"}, {name:"vanilla pod", amount:"1 seeds only"}, {name:"strong black coffee", amount:"3 tbsp"}, {name:"pot sour cream", amount:"300ml"}, {name:"milk chocolate", amount:"25g"}, {name:"double cream", amount:"200ml"}, {name:"passion fruit curd", amount:"100g"}, {name:"creme eggs", amount:"8"}, {name:"large eggs", amount:"3"}],
        instruction: "STEP 1 Heat oven to 180C/160C fan/gas 4. Line the base of a 23cm springform tin with baking parchment. Place the bourbon biscuits in a food processor and blitz until reduced to fine crumbs. Mix the melted butter and biscuit crumbs together, then press firmly onto the base of the tin with the back of a spoon. Bake in the oven for 10 mins then leave to cool while you make the filling. STEP 2 Turn oven temperature up to 240C/220C fan/gas 9. In a large bowl beat the cream cheese and sugar with an electric whisk until creamy, then add in the cocoa, vanilla, coffee, eggs, soured cream and the melted chocolate and whisk again until smooth. STEP 3 Brush the sides of a cake tin with a little more melted butter then pour in the cheesecake mixture and smooth the top with a spatula. Bake for 10 mins, then turn the heat down to 110C/90C fan/gas 1/4 for 25-30 mins. When ready the filling should be set, but with a little wobble in the middle. Turn off the oven, but leave the oven door open and allow it to cool in there for 2 hrs. Chill until ready to serve. STEP 4 To decorate the cheesecake, carefully remove it from the tin and removed the lining paper from the base. Lightly whip the double cream and place or pipe 4 large dollops of it on top of the cheesecake in a ring with 4 smaller dollops in between. Top each mound of cream with a smaller splodge of passion fruit curd and then top with the cream egg halves then place the whole ones in between. Serve immediately.",
        name: "Double chocolate Easter egg cheesecake",
        serves: 4,
        type: "NON_VEGETARIAN"
    }
]);
db.recipe.createIndex( { instruction: "text" } )
print('END #################################################################');
