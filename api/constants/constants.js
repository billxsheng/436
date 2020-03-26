module.exports.sentiments = ['Positive', 'Negative', 'Neutral'];

module.exports.errors = {
    invalidSentiment: 'Invalid sentiment. Permitted sentiments are POSITIVE, NEGATIVE, and NEUTRAL.'
}

module.exports.dbConfig = {
    contactPoints:["127.0.0.1"],
    localDataCenter: 'datacenter1',
    keyspace: 'tourdss'
}