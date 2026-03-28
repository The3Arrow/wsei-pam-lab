package lab3

class MemoryGameLogic(private val maxMatches: Int) {

    private var valueFunctions: MutableList<() -> Int> = mutableListOf()

    private var _matches: Int = 0
    var matches: Int
        get() = _matches
        set(value) { _matches = value }

    fun process(value: () -> Int): GameStates {
        if (valueFunctions.size < 1) {
            valueFunctions.add(value)
            return GameStates.Matching
        }
        valueFunctions.add(value)
        val result = valueFunctions[0]() == valueFunctions[1]()
        _matches += if (result) 1 else 0
        valueFunctions.clear()
        return when (result) {
            true -> if (_matches == maxMatches) GameStates.Finished else GameStates.Match
            false -> GameStates.NoMatch
        }
    }
}
