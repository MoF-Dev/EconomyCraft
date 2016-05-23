# Roadmap
Features of EconomyCraft will be released in "phases", with each phase consisting of fully functioning and independent sets of features.

The planned phases are:
0. Core - economy and scaffolding for the plugin
1. Markets and Banks
~1. Goods Market
~2. Goods Banking
~3. Financial Market and Banking
~4. Services Market
2. Better Economic Indicators
~1. Detailed Categorising - for better and more transparent economic growth indication
~2. Detailed Taxation
3. Artificial Intelligence
~1. Oxymoron (the Dumb Intelligence)
~~1. Trade-Bots - bots that solely exists in the markets and trade for profit to create uncertainty and fun
~2. Better Intelligence
~3. Incorporation - bots that exists and are interact-able entities in-game
~~1. Primitive survival
~~2. Architectural Revolution
~~3. Social Interaction
~~4. Functional Society
~4. Evolution
4. Sentience - this is up to your interpretation and imagination

# Conventions
Items that can be carried, such as a block of dirt or a diamond sword, are considered as "goods".

# Trading
Trading in EconomyCraft tries to imitate trading in real life with a hybrid between market trading and stock trading. A trade occurs when there exists a union between the price and quantity of the offer and bid.

1. Market Trading
~1. This involves immediate transfer of money and goods in a specific market. Since offer price is fixed in the market, market trading would mean bid price is flexible.
~2. Bid price cannot be set by the player (although the player may set a ceiling), and shall be derived from the market price.
~3. Transfer of money and goods shall happen immediately, and goods are delivered into:
~~1. The player's inventory, unless it is full, which then it is delivered into:
~~2. The player's portfolio in that particular market.

2. Stock Trading
~1. This is portfolio trading which queues bids and offers in the economy queues. Transfer of money and goods may not be immediate, and may happen during any circumstances e.g. when the player is not in a market, when the player is offline, etc.
~2. Thus, goods shall not be delivered into the player's inventory; they are delivered into the portfolio.
~3. The plugin may provide different commands for market and portfolio trading.

3. Selling (Offering)
~1. There is no guarantee for immediacy therefore offering is considered "stock trading".
~2. Players can offer goods from their inventory or their portfolio at the corresponding market. To ensure goods availability, goods are taken from the player and stored by the market until it is sold, or the player cancels the offer.

4. Portfolio
~1. Each market shall maintain a portfolio of each player. Goods obtained through portfolio trading are kept here. Goods queued for offers are NOT to be kept here.
~2. The market shall ensure players do not abuse their portfolio as some kind of a safe deposit. The plugin shall provide only withdrawing abilities to the player, although players should be able to sell goods from their portfolio.

5. Matching Algorithm
~1. The matcher identifies same goods by what is considered in game as the same item. The plugin currently uses ItemStack as identifiers.
~2. The economy shall provide intra (same-market) and inter (export) market trading, based on what is allowed by the owner of each market.
~3. The economy shall charge a transportation fee, and optionally a tariff, for inter-market trading.
~~1. The transportation fee shall be based on the difficulty of getting from the source to the destination market, and the fuel price.
~~2. The plugin shall provide an entity to represent the central Transportation Authority.
~~~1. The Transportation Authority should aim not to go bankrupt (and aim to profit), but must not exploit the fact that it is a monopoly.
~~~~1. It does not magically gain transportation abilities. It must pay for fuel.
~~~2. The Transportation Authority shall have the necessary fuel pre-stocked, before allowing deliveries of goods.
~~~3. The Transportation Authority shall publish transportation prices between all the markets in the economy, and shall accept the price of an ongoing transaction (given it is based on a price that was valid) even if the price has changed.
~~~4. Financing for roads and other infrastructure projects (AI phase) should be primarily funded, if not entirely, by the Transportation Authority.
~~~5. The non-exploitation policy implies that the Transportation Authority cannot be human.
~4. The government or the owner of the market may tax sales.
~~1. Tariff and government sales tax revenue goes to the specified government account.
~~2. Taxation and other price-interfering mechanisms shall be fully transparent to players i.e. they are included in the final price that players will choose to agree to pay.

6. Matching Algorithm (Technical)
~1. The plugin shall provide a fast and efficient matching algorithm. This means that matching may be done concurrently.
~~1. All data and data structures used by the matching algorithm shall allow concurrency.
~~~1. If a call is blocking, it shall not cause the entire pool to block.
~2. The plugin shall be accountable for all transactions.
~~1. In the case that transactions cannot be recorded, the plugin shall fall-back to lower accounting methods, but still allow the transfer of money and goods.
~3. Errors shall cause the algorithm to fail gracefully.
~~1. This means that on error, anything deducted or awarded to players shall roll-back to the previous state.
~~2. Should goods go into limbo, the most extreme level of logging (e.g. email to server administrators and operators) shall be used, and they shall restore peace, order, and accountability.

# Banking
Banks accepts deposits of money and goods, and are also financial institutions (Financial Phase) that creates credit and issues financial assets.

1. Goods Deposit
2. Money Deposit