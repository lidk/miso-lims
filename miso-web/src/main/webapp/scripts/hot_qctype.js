HotTarget.qctype = {
  createUrl: '/miso/rest/qctype',
  updateUrl: '/miso/rest/qctype/',
  requestConfiguration: function(config, callback) {
    callback(config)
  },
  fixUp: function(qctype, errorHandler) {
  },
  createColumns: function(config, create, data) {
    return [
      HotUtils.makeColumnForText('Name', true, 'name', {
        unpackAfterSave: true,
        validator: HotUtils.validator.requiredText
      }), HotUtils.makeColumnForText('Description', true, 'description', {
        unpackAfterSave: true,
      }), HotUtils.makeColumnForEnum('Target', true, true, 'qcTarget', config.qcTargets, null),
      HotUtils.makeColumnForText('Units', true, 'units', {
        unpackAfterSave: true,
      }), HotUtils.makeColumnForInt('Precision After Decimal', true, 'precisionAfterDecimal', HotUtils.validator.requiredPositiveInt),
    ];
  },

  getBulkActions: function(config) {
    return !config.isAdmin ? [] : [
        {
          name: 'Edit',
          action: function(items) {
            window.location = window.location.origin + '/miso/qctype/bulk/edit?' + jQuery.param({
              ids: items.map(Utils.array.getId).join(',')
            });
          }
        }, ];
  }
};
